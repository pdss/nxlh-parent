package com.nxlh.manager.amqp.bussiness;


import com.github.binarywang.wxpay.bean.notify.WxPayOrderNotifyResult;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dto.*;
import com.nxlh.manager.model.enums.CouponEnums;
import com.nxlh.manager.model.enums.LogLevelEnums;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.model.enums.VIPEnums;
import com.nxlh.manager.service.*;
import com.nxlh.manager.utils.TransactionUtils;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.dozer.Mapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.nxlh.manager.amqp.queue.OrderQueue.*;
import static com.nxlh.manager.rediskey.Keys.ORDER_KEY;
import static com.nxlh.manager.rediskey.Keys.SUMMARY_MONTH_ORDER_COUNT_KEY;
import static com.nxlh.manager.rediskey.Keys.SUMMARY_TODAY_ORDER_COUNT_KEY;
import static java.util.stream.Collectors.toList;

/**
 * 监听订单队列
 */
@Slf4j
@Component
public class OrderAmqpService extends BaseAmqpService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    protected Mapper beanMapper;

    @Autowired
    @Lazy
    protected TransactionUtils transactionUtils;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    @Lazy
    private WxUserRentShopService wxUserRentShopService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private LogService logService;

    @Autowired
    @Lazy
    private VipShopService vipShopService;


    //秒杀商品库存占用
    private final String MS_SHOP_STOCK_SET_KEY = "MS_SHOP_STOCK_SET_%s";

    //秒杀商品KEY
    private final String MS_PRODUCTS_KEY = "MS_PRODUCTS_%s";


    /**
     * 处理订单支付完成
     */
    @RabbitListener(queues = ORDER_SUCCESS)
    @RabbitHandler()
    public void add(WxPayOrderNotifyResult result, Channel channel, Message message) throws Exception {
        this.log.debug("==========收到消息通知，开始更新订单：{}=======", result.getOutTradeNo());
        this.log.debug(result.toString());

        try {

            var cacheKey = String.format(ORDER_KEY, result.getOutTradeNo());
            var cache = this.redisService.get(cacheKey);
            if (cache == null) {
                this.log.error("订单已过期:{}", result.getOutTradeNo());
                return;
            }

            var order = (OrderDTO) cache;

            //出租订单，出租记录
            var userRentRecords = new ArrayList<WxUserRentShopDTO>();

            var wxUser = this.wxUserService.getById(order.getWxuserid());

            order.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());
            //微信单号
            order.setThirdorderno(result.getTransactionId());
            //银行卡
            order.setBanktype(result.getBankType());
            //支付时间
            order.setPaytime(DateUtils.parseDate(result.getTimeEnd(), "yyyyMMddHHmmss"));

            order.getOrderItems().forEach((item) -> {
                item.setIstransited(0);
                item.setAddtime(new Date());
                item.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());
                item.setThirdorderno(order.getThirdorderno());
                item.setOrderno(order.getOrderno()); // 这里必须要重新设置一次，支付时有可能会产生新的订单号
            });

            BigDecimal vscore = wxUser.getVscore();
            //用户购物车中的当前商品要移除
            var removeShopFromCar = new ArrayList<String>();

            //在线支付订单,计算积分抵扣
            if (order.getOrdertype() == OrderEnums.OrderTypeEnum.sale.getValue()) {

                //如果使用了积分抵扣
                if (order.getPayscore().compareTo(BigDecimal.ZERO) == 1) {
                    vscore = vscore.subtract(order.getPayscore());
                }
                wxUser.setVscore(vscore);

                //这些商品要从购物车移除
                order.getOrderItems().forEach(e -> {
                    removeShopFromCar.add(e.getProductid());
                });


                //积分订单，扣除用户积分
            } else if (order.getOrdertype() == OrderEnums.OrderTypeEnum.score.getValue()) {
                vscore = wxUser.getVscore().subtract(order.getPayscore());
                wxUser.setVscore(vscore);
                //出租订单
            } else if (order.getOrdertype() == OrderEnums.OrderTypeEnum.lease.getValue()) {

                var userRentRecord = new WxUserRentShopDTO();
                userRentRecord.setId(IDUtils.genUUID());
                userRentRecord.setAddtime(new Date());
                userRentRecord.setOrderid(order.getId());
                userRentRecord.setProductid(order.getOrderItems().get(0).getProductid());
                userRentRecord.setStatus(OrderEnums.RentOrderStatusEnum.wait.getValue());
                userRentRecord.setWxuserid(order.getWxuserid());
                userRentRecord.setOverdueprice(BigDecimal.ZERO);
                userRentRecord.setOverduedays(0);
                userRentRecord.setProtype(order.getOrderItems().get(0).getProducttype());
                userRentRecords.add(userRentRecord);
            }


            //进行事务提交
            var flag = this.transactionUtils.transact((a) -> {

                //更新券的状态
                if (order.getUsercoupon() != null) {
                    var userCoupon = order.getUsercoupon();
                    userCoupon.setStatus(CouponEnums.CouponStatusEnum.Used.getValue());
                    this.userCouponService.updateById(userCoupon);
                }
                if (userRentRecords.size() > 0) {
                    this.wxUserRentShopService.add(userRentRecords.get(0));
                }

                this.wxUserService.updateById(wxUser);
                this.orderService.add(order);
                this.orderItemService.addBatch(order.getOrderItems());
               if(removeShopFromCar.size()>0){
                   this.shoppingCarService.removeShops(order.getWxuserid(), removeShopFromCar);
               }

            });

            if (flag) {
                //数据库写入完成后,清理Redis
                this.redisService.remove(cacheKey);

                //redis记录今日、本月订单数
//                this.redisService.incr(SUMMARY_TODAY_ORDER_COUNT_KEY);
//                this.redisService.incr(SUMMARY_MONTH_ORDER_COUNT_KEY);

                this.ackOk(message, channel);
            } else {
                this.ackFail(message, channel);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            this.log.error(ex.toString());
            this.retry(channel, message, result, RETRY_EX, ORDER_RETRY, FAIL_EX, ORDER_FAIL);
        }
    }


    /**
     * 秒杀--订单支付完成
     */
    @RabbitListener(queues = ORDER_SECKILL_SUCCESS)
    @RabbitHandler()
    public void seckillOrder(WxPayOrderNotifyResult result, Channel channel, Message message) throws Exception {
        this.log.debug("==========收到秒杀订单消息通知，开始更新订单：{}=======", result.getOutTradeNo());
        this.log.debug(result.toString());

        try {

            var cacheKey = String.format(ORDER_KEY, result.getOutTradeNo());
            var cache = this.redisService.get(cacheKey);
            if (cache == null) {
                var log = new LogDTO();
                log.setId(IDUtils.genUUID());
                log.setAddtime(new Date());
                log.setLevel(LogLevelEnums.Level.fata.getValue());
                log.setLog(new String(message.getBody()));
                this.logService.add(log);
                this.log.error("订单已过期:{}", result.getOutTradeNo());
                return;
            }

            var order = (OrderDTO) cache;



            //订单直接关闭
//            order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
            order.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());
            //微信单号
            order.setThirdorderno(result.getTransactionId());
            //银行卡
            order.setBanktype(result.getBankType());
            //支付时间
            order.setPaytime(DateUtils.parseDate(result.getTimeEnd(), "yyyyMMddHHmmss"));

            order.getOrderItems().forEach((item) -> {
                item.setIstransited(0);
                item.setAddtime(new Date());
                item.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());
                item.setThirdorderno(order.getThirdorderno());
                item.setOrderno(order.getOrderno()); // 这里必须要重新设置一次，支付时有可能会产生新的订单号
            });

            //更新库存
            var seckill_products = (List<ShopDTO>) this.redisService.get(String.format(MS_PRODUCTS_KEY, order.getSeckillId()));
            var proId = order.getOrderItems().get(0).getProductid();
            var cache_pro = seckill_products.stream().filter(e -> e.getId().equalsIgnoreCase(proId)).findFirst().get();
            //注意：这里是参与活动的商品数量
            cache_pro.getVipShop().setStockcount(cache_pro.getVipShop().getStockcount() - 1);

            //更新库存占用
            var stocks = (Map<String, Map<String, Date>>) this.redisService.get(String.format(MS_SHOP_STOCK_SET_KEY, order.getSeckillId()));
            var records = stocks.get(proId);
            records.remove(order.getId());


            //进行事务提交
            var flag = this.transactionUtils.transact((a) -> {
                this.orderService.add(order);
                this.orderItemService.addBatch(order.getOrderItems());
                //写库
                this.vipShopService.updateStock(order.getSeckillId(), order.getOrderItems().get(0).getProductid(), cache_pro.getVipShop().getStockcount());
            });

            //更新用户积分和等级
//            var orders = new ArrayList<OrderDTO>(){{
//                add(order);
//            }};
//            this.orderService.UpdateAccountLevel(orders);

            //数据库写入完成后,清理Redis
            this.redisService.remove(cacheKey);


            var seckill_key = String.format(MS_PRODUCTS_KEY, order.getSeckillId());
            this.redisService.set(seckill_key, seckill_products);

            var stock_key = String.format(MS_SHOP_STOCK_SET_KEY, order.getSeckillId());
            this.redisService.set(stock_key, stocks);


            this.ackOk(message, channel);


        } catch (Exception ex) {
            ex.printStackTrace();
            this.log.error(ex.toString());
            this.retry(channel, message, result, RETRY_EX, ORDER_RETRY, FAIL_EX, ORDER_FAIL);
        }
    }






    /**
     * 统一处理失败的记录
     */
    @RabbitListener(queues = ORDER_FAIL)
    @RabbitHandler
    public void handleFail(String json, Channel channel, Message message) throws IOException {

        try {

            var model = new MqFailDTO();
            model.setId(IDUtils.genUUID());
            model.setAddtime(DateUtils.now());
            model.setExchange(this.getOrigExchange(message.getMessageProperties(), MASTER_EX));
            model.setQueue(this.getOrigQueue(message.getMessageProperties(), ORDER_SUCCESS));
            model.setMessagedata(json);
            this.mqFailService.add(model);
            this.ackOk(message, channel);

        } catch (Exception ex) {
            ex.printStackTrace();
            this.log.error(ex.toString());
            this.ackFail(message, channel);
        }
    }


}
