package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.constant.WxPayConstants;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.config.wx.WxPayProperties;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.*;
import com.nxlh.manager.mapper.TbOrderMapper;
import com.nxlh.manager.model.dbo.*;
import com.nxlh.manager.model.dto.OrderDTO;
import com.nxlh.manager.model.dto.OrderRefundDTO;
import com.nxlh.manager.model.dto.UserCouponDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.dto.*;
import com.nxlh.manager.model.enums.CouponEnums;
import com.nxlh.manager.model.enums.ExpressEnums;
import com.nxlh.manager.model.enums.VIPEnums;
import com.nxlh.manager.model.extend.OrderMinDTO;
import com.nxlh.manager.model.extend.PayOrderDTO;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.model.to.UserTO;
import com.nxlh.manager.rediskey.Keys;
import com.nxlh.manager.service.*;
import com.nxlh.manager.utils.NxlhCommonRequest;
import com.nxlh.manager.utils.SendPost;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static com.nxlh.manager.amqp.queue.OrderQueue.ORDER_SECKILL_SUCCESS;
import static com.nxlh.manager.amqp.queue.OrderQueue.ORDER_SUCCESS;
import static com.nxlh.manager.model.enums.OrderEnums.OrderTypeEnum.seckill;
import static com.nxlh.manager.rediskey.Keys.ORDER_KEY;
import static java.util.stream.Collectors.toList;

@Service(interfaceClass = OrderService.class)
@Slf4j
public class OrderServiceImpl extends BaseDbCURDSServiceImpl<TbOrderMapper, TbOrder, OrderDTO> implements OrderService {

    @Autowired
    private OrderRefundService orderRefundService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private WxUserAddressService wxUserAddressService;

    @Autowired
    private WxUserService wxUserService;

    @Autowired
    private UserNotificationService userNotificationService;

    @Autowired
    private WebOrderService webOrderService;

    @Autowired
    @Lazy
    private SeckillService seckillService;

    /**
     * 微信支付的配置文件
     */
    @Autowired
    private WxPayProperties payProperties;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private WxUserRentShopService wxUserRentShopService;

    /**
     * 阿里云的短信key
     */
    @Value("${nxlh.sms-accesskeyid}")
    private String accessKeyId = "";
    @Value("${nxlh.sms-accesssecret}")
    private String accessKeySecret = "";

    //网站通知
    @Value("${nxlh.webshopnotify}")
    private String webshopNotify = "";

    /**
     * 开发模式
     */
    @Value("${nxlh.dev}")
    private Boolean isDev = false;


    @Value("${nxlh.userRedisExprie}")
    private long userRedisExprie = 20L;

    /**
     * 申请退款回调通知
     */
    @Value("${nxlh.wechat.pay.refundNotifyUrl}")
    private String refunNotifyUrl = "";

    /**
     * 秒杀订单支付通知
     */
    @Value("${nxlh.wechat.pay.secKillOrderNotifyUrl}")
    private String secKillOrderNotifyUrl = "";

    /**
     * 预订单缓存时间，10Min
     */
    private final long Order_Redis_Timeout = 5L;

    //秒杀商品库存占用
    private final String MS_SHOP_STOCK_SET_KEY = "MS_SHOP_STOCK_SET_%s";

    //秒杀商品KEY
    private final String MS_PRODUCTS_KEY = "MS_PRODUCTS_%s";

    //秒杀活动key
    private final String MS_SECKILL_KEY = "MS_SECKILL_%s";

    //秒杀订单锁
    private static Object locker = new Object();

    //同步积分锁
    private static Object syncScoreLocker = new Object();

    //web url
    @Value("${nxlh.checkVipIsInvalidation}")
    private String checkVipIsInvalidationUrl;

    /**
     * 订单详情
     *
     * @param orderid
     * @return
     */
    @Override
    public OrderDTO getDetails(String orderid) {
        var order = this.baseMapper.getDetails(orderid);
        var orderDTO = this.beanMap(order, this.currentDTOClass());
        //计算出租订单的逾期天数和逾期金额
        if (orderDTO.getOrdertype().equals(OrderEnums.OrderTypeEnum.lease.getValue())) {
            orderDTO.setOverdueMoney(BigDecimal.ZERO);
            orderDTO.setOverdue(0);
            //订单没有结束则时间为当前时间，若订单结束则时间为订单结束时间
            if (orderDTO.getStatus().equals(OrderEnums.OrderStatusEnum.complete.getValue())) {
                //如果订单已经完成则按照退款金额和实际押金计算逾期金额
                TbOrderRefund tbOrderRefund = orderDTO.getRefundItems().get(0);
                if (tbOrderRefund.getRentcost() != null) {
                    BigDecimal subtract = tbOrderRefund.getRentcost().subtract(tbOrderRefund.getRefundprice());
                    orderDTO.setOverdueMoney(subtract);
                }
            } else if (orderDTO.getIstransited() > 0) {//是否发货
                //在确认收货日期的基础上再增加7天用于出租回收的快递时间
                Date date = DateUtils.addDay(orderDTO.getConfirmtime(), 7);
                long l = DateUtils.daysBetween(date, new Date());
                if (l > 0) {
                    int days = (int) l - orderDTO.getRentdays();
                    //如果还未到达对应的
                    if (days > 0) {
                        BigDecimal subtract = orderDTO.getRentpricebyday().subtract(new BigDecimal(days));
                        orderDTO.setOverdueMoney(subtract);
                    }
                    orderDTO.setOverdue(days);
                }
            }
        }

        return orderDTO;
    }

    @Override
    public OrderDTO getOrderInfoWithStatus(String orderid) {
        var order = this.getDetails(orderid);

        var before7Days = DateUtils.subDays(7);
        var before8Days = DateUtils.subDays(8);
        if (order.getStatus() == OrderEnums.OrderStatusEnum.transit.getValue() && order.getDeliverytime().compareTo(before7Days) < 1) {
            order.setStatus(OrderEnums.OrderStatusEnum.received.getValue());
            //订单是已收货的，但是收货时间超过8天
        } else if (order.getStatus() == OrderEnums.OrderStatusEnum.received.getValue() && order.getConfirmtime().compareTo(before8Days) < 1) {
            order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
        }
        return order;
    }


    /**
     * 发货
     *
     * @param id            id
     * @param express       快递，详见 ExpressEnums
     * @param trackernumber 快递单号
     * @return
     */
    @Override
    public MyResult transit(String id, int express, String trackernumber) {
        var tbOrderItem = orderItemService.getById(id);
        var order = this.baseMapper.selectByPrimaryKey(tbOrderItem.getOrderid());

        //筛选该订单下所有的未发货的子订单,不包括已退款
//        Example example = Example.builder(TbOrderItem.class)
//                .where(Sqls.custom().andEqualTo("orderid", tbOrderItem.getOrderid()).andEqualTo("status", 1)).build();
        Example example = Example.builder(TbOrderItem.class)
                .where(Sqls.custom()
                        .andEqualTo("orderid", tbOrderItem.getOrderid())
                        .andEqualTo("istransited", 0)
                        .andNotEqualTo("status", OrderEnums.OrderStatusEnum.refundsuccess.getValue())).build();
        List<OrderItemDTO> tbOrderItems = this.orderItemService.list(example);

        var updateResult = false;

        //更新orderitem表的发货记录
        if (tbOrderItem.getStatus() == OrderEnums.OrderStatusEnum.ready.getValue()) {
            tbOrderItem.setExpress(express); //快递
            tbOrderItem.setTracknumber(trackernumber);    //运单号
            tbOrderItem.setDeliverytime(DateUtils.now());     //发货时间
            tbOrderItem.setIstransited(1);
            //秒杀订单不更新订单状态
            if (order.getOrdertype() != seckill.getValue()) {
                tbOrderItem.setStatus(OrderEnums.OrderStatusEnum.transit.getValue());
            } else {
                tbOrderItem.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
            }


            //更新order表的发货记录
            order.setIstransited(1);
            order.setExpress(express);

            //判断是否子订单是否已经全部发货,排除当前订单项
            if (tbOrderItems.size() - 1 <= 0) {
                order.setDeliverytime(new Date());
                //秒杀订单不更行订单状态
                if (order.getOrdertype() != seckill.getValue()) {
                    order.setStatus(OrderEnums.OrderStatusEnum.transit.getValue());
                } else {
                    order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
                }
            }
            updateResult = this.transactionUtils.transact((a) -> {
                this.orderItemService.updateById(tbOrderItem);
                this.baseMapper.updateByPrimaryKey(order);

            });
            //发货成功，发送消息
            if (updateResult) {
                this.userNotificationService.sendMsgByTransit(order.getWxuserid(), tbOrderItem.getProductname(), tbOrderItem.getId());
            }

        }

        return updateResult ? MyResult.ok(HttpResponseEnums.Ok.getValue()) : MyResult.build(HttpResponseEnums.BadRequest.getValue(), "参数无效");
    }


    /**
     * 根据特定分类获取订单列表(分页)
     *
     * @param page   分页参数
     * @param filter wait refund  yes
     * @return
     */
    @Override
    public MyResult getByFilter(PageParameter page, String filter) {
        List<TbOrder> orders = null;
        PageHelper.startPage(page.getPageIndex() + 1, page.getPageSize());
        Example.Builder example = null;
        switch (filter) {
            case "wait": //待发货,按下单时间升序
                example = this.sqlBuilder().where(Sqls.custom().andEqualTo("status", 1))
                        .orderByAsc("addtime");
                orders = this.baseMapper.selectByExample(example.build());
                break;

            case "refund": //申请退款的订单
                orders = this.baseMapper.getWaitRefundOrders();
                break;
            default: //默认查询所有订单
                orders = this.baseMapper.getAllDetailOrders();
                break;
        }

        PageInfo<TbOrder> pageInfo = new PageInfo<TbOrder>(orders);
        var dtos = this.beanListMap(pageInfo.getList(), this.currentDTOClass());
        var dtoPage = this.beanMap(pageInfo, PageInfo.class);
        dtoPage.setList(dtos);
        return MyResult.ok(dtoPage);

    }


    /**
     * 同意退款
     *
     * @param orderRefundId 退款记录id
     * @param refundPrice   退款金额
     * @return
     */
    @Override
    public MyResult accessRefund(String orderRefundId, BigDecimal refundPrice) throws WxPayException {

        //不能小于1分钱
//        if (refundPrice.compareTo(BigDecimal.valueOf(0.01)) < 0) {
//            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "退款金额不能小于0.01元", null);
//        }

        //退款记录
        var orderRefund = this.orderRefundService.getById(orderRefundId);
        //对应的订单信息
        var order = this.getDetails(orderRefund.getOrderid());

        var wxUser = this.wxUserService.getById(order.getWxuserid());

        var updateFlag = false;
        WxPayRefundResult refundResult = null;
        //待审核状态，继续执行
        if (orderRefund.getStatus() == 0 && refundPrice.compareTo(BigDecimal.valueOf(0.01)) > 0) {

            this.log.debug("==========================开始退款=========================");

            WxPayRefundRequest request = WxPayRefundRequest.newBuilder()
                    .transactionId(order.getThirdorderno())
                    .notifyUrl(this.refunNotifyUrl)
                    .totalFee(order.getPayprice().multiply(BigDecimal.valueOf(100)).intValue()) //转分
                    .outRefundNo(orderRefund.getRefundno())
                    .refundFee(refundPrice.multiply(BigDecimal.valueOf(100)).intValue())
                    .build();

            refundResult = this.wxPayService.refund(request);
            if (refundResult.getReturnCode().equals("SUCCESS")) {
                this.log.debug("==========================退款成功=========================");
                updateFlag = true;
            } else {
                this.log.error("微信退款异常:{}\n{}", refundResult.getReturnMsg(), refundResult.getErrCodeDes());
                return MyResult.error("退款失败!");
            }

        } else if (orderRefund.getStatus() != 0) {
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "该订单已处理!请勿重复提交", order);
        } else {
            //0元申请退款
            updateFlag = true;
        }


        orderRefund.setStatus(OrderEnums.OrderRefundEnum.allow.getValue());
        orderRefund.setRefundprice(refundPrice);
        orderRefund.setVerifytime(DateUtils.now());
        orderRefund.setThirdrefundno(refundResult == null ? "" : refundResult.getRefundId()); //微信提供的退款单号
        orderRefund.setRefundscore(BigDecimal.ZERO);

        //订单中使用的券
//                final List<UserCouponDTO> userCoupon = new ArrayList<>();

        // 判断当前订单是否是全部退款
        // 如果是：订单状态更新为：交易关闭
        // 否则：订单不做任何更新,只更新订单项
        //退款：不返还任何积分和优惠券

        //已通过的退款申请数
        var sumRefundCount = order.getRefundItems().stream().filter(e -> e.getStatus() == 1).mapToInt(e -> e.getRefundcount()).sum();
        //nowprice金额减去退款金额
        order.setNowprice(order.getNowprice().subtract(refundPrice));
        //全部都退款了
        if (order.getOrderItems().size() == sumRefundCount + 1) {
            order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
            order.setRefund(OrderEnums.OrderRefundFlagEnum.all.getValue());
        } else {
            //记录：订单有部分退款
            order.setRefund(OrderEnums.OrderRefundFlagEnum.part.getValue());
        }
        //订单中对应的商品记录
        var orderItem = order.getOrderItems().stream().filter(e -> e.getId().equalsIgnoreCase(orderRefund.getOrderitemid())).findFirst().get();
        //该商品已完成退款，更新状态
        orderItem.setStatus(OrderEnums.OrderStatusEnum.refundsuccess.getValue());


        //事务提交
        var result = this.transactionUtils.transact((p) -> {
            this.orderRefundService.updateById(orderRefund);
            this.orderItemService.updateById(orderItem);
            this.updateById(order);
            this.wxUserService.updateById(wxUser);
        });
        var refreshOrder = this.getDetails(order.getId());
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, refreshOrder);
    }


    /**
     * 拒绝退款
     *
     * @param orderRefundId 退款记录id
     * @param refundReason  拒绝原因
     * @return
     */
    @Override
    public MyResult rejectRefund(String orderRefundId, String refundReason) {
        var orderRefund = this.orderRefundService.getById(orderRefundId);
        var order = this.getDetails(orderRefund.getOrderid());
        if (orderRefund.getStatus() == 0) {
            orderRefund.setStatus(2);
            orderRefund.setRefuse(refundReason);
            this.orderRefundService.updateById(orderRefund);
            //刷新下订单
            order = this.getDetails(orderRefund.getOrderid());
            return MyResult.ok(order);
        } else {
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "该订单已处理!请勿重复提交", order);
        }
    }


    /**
     * 自动确认收货，默认8天
     *
     * @return
     */
    @Override
    public boolean autoConfirmReceiveOrder() {
        //当前时间为准，8天前的今天
        Date date = DateUtils.subDays(7);//获得7天前的0点0分(包含今天)
        //构建sql,把10天前已收货的订单自动关闭
        var example = this.sqlBuilder()
                .where(
                        Sqls.custom()
                                .andEqualTo("status", OrderEnums.OrderStatusEnum.transit.getValue())
                                .andLessThanOrEqualTo("deliverytime", date)).build();

        var orders = this.list(example);
        boolean result = this.transactionUtils.transact((a) -> {
            orders.forEach(o -> {
                this.confirmReceive(o.getId(), OrderEnums.OrderConfirmTypeEnum.auto);
            });
        });
        return result;
    }


    /**
     * 自动关闭订单，默认8天，次日开始算
     */
    @Override
    public boolean autoCloseOrder() {
        //当前时间为准，8天前的今天
        Date date = DateUtils.subDays(7);//获得7天前的零点零分(算上几天)
        //构建sql,把10天前已收货的订单自动关闭,消费订单，租赁和积分不参与
        var example = this.sqlBuilder()
                .where(
                        Sqls.custom()
                                .andEqualTo("status", OrderEnums.OrderStatusEnum.received.getValue())
                                .andEqualTo("ordertype", OrderEnums.OrderTypeEnum.sale.getValue())
                                .andLessThanOrEqualTo("confirmtime", date)).build();

        var example2 = this.sqlBuilder()
                .where(
                        Sqls.custom()
                                .andEqualTo("status", OrderEnums.OrderStatusEnum.received.getValue())
                                .andEqualTo("ordertype", seckill.getValue())
                                .andLessThanOrEqualTo("confirmtime", date)).build();

        //马上要关闭的订单
        var orders = this.list(example);
        var seckillOrders = this.list(example2);

        if (!CollectionUtils.isEmpty(orders) && !CollectionUtils.isEmpty(seckillOrders)) {
            orders.addAll(seckillOrders);
        } else if (!CollectionUtils.isEmpty(seckillOrders)) {
            orders = seckillOrders;
        }
        if (!CollectionUtils.isEmpty(orders)) {
            this.UpdateAccountLevel(orders);
        }
        return true;
    }


    public void UpdateAccountLevel(List<OrderDTO> orders) {
        var wxUserIds = orders.stream().map(o -> o.getWxuserid()).distinct().collect(toList());
        var wxusers = this.wxUserService.list(this.sqlBuilder().where(Sqls.custom().andIn("id", wxUserIds)).build());
        var now = DateUtils.now();
        orders.forEach(order -> {
            //订单项
            var orderItems = this.orderItemService.list(new HashMap<String, Object>() {{
                put("orderid", order.getId());
            }}, ObjectUtils.toArray("addtime"), 1);


            //会员用户,算积分
            BigDecimal score = BigDecimal.ZERO;
            var user = wxusers.stream().filter(e -> e.getId().equalsIgnoreCase(order.getWxuserid())).findFirst().get();
            var payPrice = order.getNowprice().subtract(order.getExpressprice());//不算快递费

            switch (user.getVipid()) {
                case 1:
                    score = payPrice.divide(BigDecimal.valueOf(100));
                    break;
                case 2:
                    score = payPrice.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(1.5));
                    break;
                case 3:
                    score = payPrice.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(2));
                    break;
                default:
                    break;
            }
            //判断用户原先是否经验值
            if (user.getExp() == null) {
                user.setExp(BigDecimal.ZERO);
            }
            //普通订单并且子订单数大于1且订单有过退款
            if (order.getOrdertype().equals(OrderEnums.OrderTypeEnum.sale.getValue()) && orderItems.size() > 1 && !(order.getRefund().equals(OrderEnums.OrderRefundFlagEnum.none.getValue()))) {
                //订单全部退款
                if (order.getRefund().equals(OrderEnums.OrderRefundFlagEnum.all)) {
                    user.setExp(user.getExp().add(BigDecimal.ZERO));
                } else {//订单部分退款
                    orderItems.forEach(orderItemInfo -> {
                        if (orderItemInfo.getStatus().equals(OrderEnums.OrderStatusEnum.refundsuccess.getValue())) {
                            user.setExp(user.getExp().add(BigDecimal.ZERO));
                        } else if (orderItemInfo.getStatus().equals(OrderEnums.OrderStatusEnum.received.getValue())) {
                            //商品金额在订单总金额(没有优惠)的占比*优惠金额 获得单间商品的优惠金额
                            BigDecimal reduce = BigDecimal.ZERO;
                            //判断是有优惠券还是积分
                            if (order.getCheap().compareTo(BigDecimal.ZERO) > 0) {//优惠券
                                reduce = orderItemInfo.getProductprice().divide(order.getOrderprice(), 0, RoundingMode.FLOOR).multiply(order.getCheap());
                            } else {//积分
                                reduce = orderItemInfo.getProductprice().divide(order.getOrderprice(), 0, RoundingMode.FLOOR).multiply(order.getPayscore());
                            }

                            //商品金额减去优惠金额，单个商品的实际支付金额
                            BigDecimal subtract = orderItemInfo.getProductprice().subtract(reduce);
                            //软件算2倍经验
                            if (orderItemInfo.getProducttype().equals(1)) {
                                subtract = subtract.multiply(new BigDecimal(2));
                            }

                            user.setExp(user.getExp().add(subtract));
                        }
                    });
                }
            } else {
                //获取订单中已完成的软件订单
                BigDecimal reduce = orderItems.stream()
                        .filter(e -> e.getProducttype() == 1 && e.getStatus().equals(OrderEnums.OrderStatusEnum.received.getValue()))
                        .map(e -> e.getProductprice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .multiply(new BigDecimal(2));
                //获取订单中已完成的硬件订单
                BigDecimal reduce1 = orderItems.stream()
                        .filter(e -> e.getProducttype() == 2 && e.getStatus().equals(OrderEnums.OrderStatusEnum.received.getValue()))
                        .map(e -> e.getProductprice())
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                BigDecimal productprice = reduce.add(reduce1);
                user.setExp(user.getExp().add(productprice));
            }

            //累计消费
            user.setSumpay(payPrice.add(user.getSumpay()));

            //非普通用户
            if (user.getVipid() != VIPEnums.VIPTypeEnum.None.getValue()) {
                //只有会员才能算积分
                order.setScore(score);
                //更新用户积分
                user.setVscore(score.add(user.getVscore()));
            }

            if (orderItems != null && orderItems.size() > 0) {
                orderItems.forEach(e -> {
                    //非退款完成，改为交易关闭
                    if (e.getStatus() != OrderEnums.OrderStatusEnum.refundsuccess.getValue()) {
                        e.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
                        e.setClosetime(now);
                    }
                });
            }

            //更新状态:交易关闭,记录时间
            order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
            order.setClosetime(now);
        });

        //最后根据用户的经验值决定会员等级
        wxusers.forEach(e -> {
            //检查用户会员等级，根据累计消费情况更新
            var level = this.getVipLevel(e.getId(), e.getExp());
            if (e.getVipid() < level) {
                e.setVipid(level);
            }
        });

        final var final_orders = orders;

        boolean result = this.transactionUtils.transact((a) -> {

            this.updateBatchById(final_orders);
            this.wxUserService.updateBatchById(wxusers);
        });

        if (result) {
            this.log.info("关闭订单{}个", orders.size());
        }
        //更新缓存中的用户信息
        wxusers.forEach(e -> {
            if (this.redisService.exists(e.getSessiontoken())) {

                //fix emoji
                e.setNickname(this.emojiConverter.toAlias(e.getNickname()));
                this.redisService.set(e.getSessiontoken(), e, userRedisExprie, TimeUnit.DAYS);
            }
        });
        if (result) {
            this.sendWebUser(wxusers, "wxoverorder");
        }
    }


    /**
     * 预生成订单信息
     *
     * @return
     */
    @Override
    public MyResult preview(String wxUserId, Map<String, Integer> products) {
        try {
            var wxUser = this.wxUserService.getById(wxUserId);
            final var order = new OrderDTO();
            order.setWxopenid(wxUser.getOpenid());
            order.setWxuserid(wxUserId);
            order.setExpress(ExpressEnums.Express.zto.getValue());
            order.setRefund(OrderEnums.OrderRefundFlagEnum.none.getValue());
            order.setOrdertype(OrderEnums.OrderTypeEnum.sale.getValue());
            order.setOrderno(GenerateNum.getInstance().GenerateOrder());
            order.setId(IDUtils.genUUID());
            order.setAddtime(new Date());
            order.setCancoupon(1); // 默认允许使用优惠券
            order.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());

            //获取对应商品的信息
            var filter = Example.builder(TbShop.class)
                    .where(Sqls.custom()
                            .andIn("id", products.keySet())
                            .andEqualTo("status", 1)
                            .andEqualTo("issale", 1)
                            .andEqualTo("isdelete", 0)).build();
            var productList = this.shopService.list(filter);
            var orderItems = new ArrayList<OrderItemDTO>();

            var proIds = productList.stream().map(e -> e.getId()).collect(toList());

            //设置购买数量,对应的商品
            productList.forEach(e -> {
                e.setBuyCount(products.get(e.getId()));
                //只要存在不允许使用券的商品，则当前订单都不能用券
                if (e.getCancoupon() == 0) {
                    order.setCancoupon(0);
                }
                var o_item = new OrderItemDTO();
                o_item.setProduct(e);
                o_item.setId(IDUtils.genUUID());
                o_item.setBuycount(e.getBuyCount());
                o_item.setExpress(order.getExpress());
                o_item.setIstransited(0);
                o_item.setOrderid(order.getId());
                o_item.setOrderno(order.getOrderno());
                o_item.setProductid(e.getId());
                o_item.setProductname(e.getShopname());
                o_item.setProductimage(e.getThumbnails());
                o_item.setWxuserid(wxUserId);
                o_item.setExchangescore(BigDecimal.ZERO);
                o_item.setAddtime(new Date());
                o_item.setProducttype(e.getGenres());

                o_item.setProductprice(this.computeProductPrice(e, null));

                o_item.setStatus(OrderEnums.OrderStatusEnum.nopay.getValue());
                o_item.setTracknumber("");
                o_item.setSumprice(BigDecimal.valueOf(e.getBuyCount()).multiply(o_item.getProductprice()));
                orderItems.add(o_item);

            });
            order.setOrderItems(orderItems);

            //用户的收货地址
            var address = this.wxUserAddressService.getDefaultAddr(wxUserId, true);
            //新用户可能没有填写收货地址
            if (address != null) {
                order.setReceiveaddress(address.getAddress());
                order.setReceivearea(address.getArea());
                order.setReceivecity(address.getCity());
                order.setReceivephoneprefix(address.getPhoneprefix());
                order.setReceiveprovince(address.getProvince());
                order.setReceivername(address.getUsername());
                order.setReceiverphone(address.getPhone());
                order.setReceiveAddressId(address.getId());
            }

            //商品总价
            var allPrice = this.computeAllProductsPrice(order);
            order.setOrderprice(allPrice);

            //要使用的券
            UserCouponDTO useCoupon = null;
            if (order.getCancoupon() == 1) {
                //获取用户的优惠券
                var userCoupons = this.userCouponService.getCouponsByStatus(new PageParameter(1, 100), wxUserId, CouponEnums.CouponStatusEnum.Valid).getList();
                if (!CollectionUtils.isEmpty(userCoupons)) {
                    //可用的满减券,通用券或者包含指定商品的满减券,按优惠金额排序
                    var validCoupons = userCoupons.stream()
                            .filter(e -> e.getCouponInfo().getLimitmoney().compareTo(allPrice) < 1 && (e.getCouponInfo().getShopscope().equalsIgnoreCase("all") || proIds.contains(e.getCouponInfo().getShopscope())))
                            .sorted((a, b) -> 0 - a.getCouponInfo().getPrice().compareTo(b.getCouponInfo().getPrice()))
                            .collect(toList());
                    //有可用的券
                    if (!CollectionUtils.isEmpty(validCoupons)) {
                        useCoupon = validCoupons.get(0); // 最大的满减券
                    } else {

                        //有对应的免费券
                        var freeCoupons = userCoupons.stream().filter(e -> {
                            for (var p : productList) {
                                if (p.getId().equalsIgnoreCase(e.getCouponInfo().getShopscope())) {
                                    return true;
                                }
                            }
                            return false;
                        }).collect(toList());

                        useCoupon = freeCoupons.size() > 0 ? freeCoupons.get(0) : null;
                    }

                }
                //可使用的积分抵扣
                var maxScore = this.computeOrderSubtractScore(allPrice, wxUser.getVscore());
                order.setMaxScore(maxScore);
            }


            //计算应付金额,默认不使用积分，用券
            var final_order = this.computePayPrice(wxUser, order, useCoupon, false);

            //放入Redis,存放5min
            this.redisService.set(String.format(ORDER_KEY, order.getOrderno()), final_order, Order_Redis_Timeout, TimeUnit.MINUTES);
            return MyResult.ok(order.getOrderno());
        } catch (Exception ex) {
            ex.printStackTrace();
            return MyResult.error("系统异常:" + ex.toString());
        }
    }


    /**
     * 预生成订单----秒杀订单
     *
     * @param wxUserId
     * @param sid
     * @param productId
     * @return
     */
    @Override
    public MyResult previewByMS(String wxUserId, String sid, String productId) {
        synchronized (locker) {

            try {
                if (!this.checkMSStock(wxUserId, sid, productId)) {
                    return MyResult.forb("商品已售完");
                }

                var wxUser = this.wxUserService.getById(wxUserId);
                final var order = new OrderDTO();
                order.setSeckillId(sid);
                order.setWxopenid(wxUser.getOpenid());
                order.setWxuserid(wxUserId);
                order.setExpress(ExpressEnums.Express.zto.getValue());
                order.setRefund(OrderEnums.OrderRefundFlagEnum.none.getValue());
                order.setOrdertype(seckill.getValue());
                order.setOrderno(GenerateNum.getInstance().GenerateOrder());
                order.setId(IDUtils.genUUID());
                order.setAddtime(new Date());
                order.setCancoupon(0); // 不允许使用优惠券
                order.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());
                order.setClosetime(new Date());
                order.setDeliverytime(new Date());
                order.setConfirmtime(new Date());
                order.setConfirmtype(OrderEnums.OrderConfirmTypeEnum.auto.getValue());

                var product = this.shopService.getVipShopById(sid, productId);
                if (product.getVipShop().getStatus() == 0) {
                    return MyResult.forb("该商品已下架");
                }
                var orderItems = new ArrayList<OrderItemDTO>();

                product.setBuyCount(1);
                var o_item = new OrderItemDTO();
                o_item.setProduct(product);
                o_item.setId(IDUtils.genUUID());
                o_item.setBuycount(product.getBuyCount());
                o_item.setExpress(order.getExpress());
                o_item.setIstransited(0);
                o_item.setOrderid(order.getId());
                o_item.setOrderno(order.getOrderno());
                o_item.setProductid(product.getId());
                o_item.setProductname(product.getShopname());
                o_item.setProductimage(product.getThumbnails());
                o_item.setWxuserid(wxUserId);
                o_item.setExchangescore(BigDecimal.ZERO);
                o_item.setAddtime(new Date());
                o_item.setProducttype(product.getGenres());
                //商品价格
                o_item.setProductprice(product.getVipShop().getVipprice());

                o_item.setStatus(OrderEnums.OrderStatusEnum.nopay.getValue());
                o_item.setTracknumber("");
                o_item.setSumprice(BigDecimal.valueOf(product.getBuyCount()).multiply(o_item.getProductprice()));
                orderItems.add(o_item);

                order.setOrderItems(orderItems);

                //用户的收货地址
                var address = this.wxUserAddressService.getDefaultAddr(wxUserId, true);
                //新用户可能没有填写收货地址
                if (address != null) {
                    order.setReceiveaddress(address.getAddress());
                    order.setReceivearea(address.getArea());
                    order.setReceivecity(address.getCity());
                    order.setReceivephoneprefix(address.getPhoneprefix());
                    order.setReceiveprovince(address.getProvince());
                    order.setReceivername(address.getUsername());
                    order.setReceiverphone(address.getPhone());
                    order.setReceiveAddressId(address.getId());
                }

                //商品总价
                //目前只能单个购买，不能使用任何优惠
                var allPrice = product.getVipShop().getVipprice();
                order.setOrderprice(allPrice);
                order.setPayprice(allPrice);
                //全部秒杀的库存占用
                var all_ms_product_stocks = (Map<String, Map<String, Date>>) this.redisService.get(String.format(MS_SHOP_STOCK_SET_KEY, sid));
                if (MapUtils.isEmpty(all_ms_product_stocks)) {
                    all_ms_product_stocks = new HashMap<>();
                }
                //当前商品的占用
                var stocks = all_ms_product_stocks.get(productId);
                if (CollectionUtils.isEmpty(stocks)) {
                    stocks = new HashMap<>();
                }
                stocks.put(order.getId(), DateUtils.addMinute(new Date(), 5));
                all_ms_product_stocks.put(productId, stocks);

                //放入Redis,存放5min
                this.redisService.set(String.format(ORDER_KEY, order.getOrderno()), order, Order_Redis_Timeout, TimeUnit.MINUTES);
                this.redisService.set(String.format(MS_SHOP_STOCK_SET_KEY, sid), all_ms_product_stocks);

                return MyResult.ok(order.getOrderno());
            } catch (Exception ex) {
                ex.printStackTrace();
                return MyResult.error("系统异常:" + ex.toString());
            }
        }
    }


    /**
     * 获取预览订单
     *
     * @param orderno
     * @return
     */
    public MyResult getPreview(String orderno) {
        var cacheKey = String.format(ORDER_KEY, orderno);
        var cache = this.redisService.get(cacheKey);
        if (cache == null) {
            return MyResult.error("订单无效，请重新下单");
        } else {
            return MyResult.ok(cache);
        }
    }


    /**
     * 计算商品总价
     *
     * @param order
     * @return
     */
    private BigDecimal computeAllProductsPrice(OrderDTO order) {
        return computeAllProductsPrice(order.getOrderItems().stream());
    }

    /**
     * 单个商品价格
     *
     * @param p
     * @return
     */
    private BigDecimal computeProductPrice(TbShop p, BigDecimal count) {
        if (count == null) {
            count = BigDecimal.ONE;
        }
        //预售商品==>预售价
        if (p.getIsprebuy() == 1) {
            return p.getPrebuyprice().multiply(count);
            //打折商品==>折扣价
        } else if (p.getDiscount().doubleValue() != 1) {
            //折扣*原价
            var price = p.getSaleprice().multiply(BigDecimal.valueOf(p.getDiscount())).multiply(count);
            //四舍五入，保留2位小数
            return price.setScale(2, RoundingMode.HALF_UP);
            //原价销售
        } else {
            return p.getSaleprice().multiply(count);
        }
    }

    private BigDecimal computeAllProductsPrice(Stream<OrderItemDTO> shops) {
        //商品总价（无优惠）
        var sumPrice = shops.map(p -> {
            var count = BigDecimal.valueOf(p.getBuycount());

            return this.computeProductPrice(p.getProduct(), count);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);

        return sumPrice.setScale(2, RoundingMode.HALF_UP);
    }


    /**
     * 计算订单可抵扣的最大积分
     *
     * @param allPrice
     * @param userScore
     * @return
     */
    private BigDecimal computeOrderSubtractScore(BigDecimal allPrice, BigDecimal userScore) {
        //最多可使用的积分抵扣,积分抵扣最多是订单的20%
        var maxScore = allPrice.multiply(BigDecimal.valueOf(0.2)).setScale(0, RoundingMode.DOWN);
        //如果用户的积分不足以全部抵扣(<20%),则最多使用用户的全部积分
        if (userScore.compareTo(maxScore) < 0) {
            maxScore = userScore;
        }
        return maxScore;
    }

    /**
     * 计算订单应付金额
     *
     * @param order         订单信息
     * @param userCouponDTO 用户优惠券
     * @param isscore       是否使用积分抵扣
     * @return
     */
    private OrderDTO computePayPrice(WxUserDTO wxUser, OrderDTO order, UserCouponDTO userCouponDTO, boolean isscore) {

        //应付金额
        var allPrice = this.computeAllProductsPrice(order);
        order.setPayscore(BigDecimal.ZERO);

        //商品总价+邮费，不计算减免费用
        var orderPrice = allPrice;

        var express_price = BigDecimal.ZERO;

        //快递费
        if (order.getExpress() == ExpressEnums.Express.zto.getValue()) {
            order.setExpressprice(BigDecimal.ZERO);
        } else {
            express_price = BigDecimal.valueOf(20);
            order.setExpressprice(express_price);
        }

        //region 支付订单类型,计算价格，优惠券
        if (order.getOrdertype() == OrderEnums.OrderTypeEnum.sale.getValue() || order.getOrdertype() == seckill.getValue()) {
            //使用积分抵扣
            if (isscore) {

                var maxScore = this.computeOrderSubtractScore(allPrice, wxUser.getVscore());

                //最多能抵扣的积分
                order.setMaxScore(maxScore);

                allPrice = allPrice.subtract(maxScore);

                order.setCouponid("");//积分抵扣和优惠券二选一
                order.setUsercoupon(null);
                order.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());
                order.setMaxScore(maxScore);
                order.setPayscore(maxScore);

                //没有用券
            } else if (userCouponDTO == null) {
                order.setPayprice(orderPrice.setScale(2, RoundingMode.HALF_UP));
                order.setOrderprice(orderPrice.setScale(2, RoundingMode.HALF_UP));
                order.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());
                order.setCouponid("");
                order.setUsercoupon(null);
                //return order;
            } else {

                //满减券
                if (userCouponDTO.getCouponInfo().getType() == CouponEnums.CouponTypeEnum.Limit.getValue()) {
                    var canUse = false;
                    var proIds = order.getOrderItems().stream().map(e -> e.getProductid()).collect(toList());

                    //全场通用或指定商品,符合满减条件，不包括邮费
                    if ((userCouponDTO.getCouponInfo().getShopscope().equalsIgnoreCase(CouponEnums.CouponShopScoreEnum.all.getValue()) ||
                            proIds.contains(userCouponDTO.getCouponInfo().getShopscope()))
                            && userCouponDTO.getCouponInfo().getLimitmoney().compareTo(allPrice) < 1) {
                        canUse = true;

                        //指定商品，目前还没有这种情况：同一个游戏买多个
                    } else if (userCouponDTO.getCouponInfo().getShopscope() == CouponEnums.CouponShopScoreEnum.shopid.getValue()) {

                        //指定商品标签
                    } else if (userCouponDTO.getCouponInfo().getShopscope().equalsIgnoreCase(CouponEnums.CouponShopScoreEnum.tag.getValue())) {
                        //当前订单中的所有商品id
                        var orderItemProductIds = order.getOrderItems().stream().map(i -> i.getProductid()).collect(toList());
                        //获取订单中符合当前券的商品
                        var productInfos = this.userCouponService.getTagProductInfoByCouponId(userCouponDTO.getId(), orderItemProductIds);

                        if (!CollectionUtils.isEmpty(productInfos)) {
                            var _validProductIds = productInfos.stream().map(p -> p.getId()).collect(toList());
                            //对应当前订单项
                            var orderItems = order.getOrderItems().stream().filter(i -> _validProductIds.contains(i.getProductid()));

                            //实际可用券的商品总价
                            var sumprice = computeAllProductsPrice(orderItems);
                            //可用券的商品总价满足减免标准
                            if (sumprice.compareTo(userCouponDTO.getCouponInfo().getLimitmoney()) > -1) {
                                canUse = true;
                            }
                        }
                    }

                    if (canUse) {
                        allPrice = (allPrice.subtract(userCouponDTO.getCouponInfo().getPrice()));
                        order.setCouponid(userCouponDTO.getId());
                        order.setUsercoupon(userCouponDTO);
                        order.setCheap(userCouponDTO.getCouponInfo().getPrice());
                        order.setCoupontype(CouponEnums.CouponTypeEnum.Limit.getValue());

                    }

                    //免费券,目前只限单个商品
                } else {
                    this.log.debug("================无效券==============");
                }
            }
        }
        //endregion

        //region 出租订单
        else if (order.getOrdertype() == OrderEnums.OrderTypeEnum.lease.getValue()) {
//            allPrice = allPrice.multiply(BigDecimal.valueOf(0.7));
//            orderPrice = allPrice;
            var o_item = order.getOrderItems().get(0);
            allPrice = o_item.getProductprice();
            var product = this.shopService.getById(o_item.getProductid());

            if (product.getGenres() == 1) {
                var rentdays = order.getRentterm() == 1 ? 30 : order.getRentterm() == 2 ? 90 : order.getRentterm() == 3 ? 180 : 365;
                order.setRentdays(rentdays);
                var rentprice = this.computeRentPriceByTerm(product.getSaleprice(), order.getRentterm());
                order.setRentprice(rentprice);
                //每日租金
                order.setRentpricebyday(this.computeRentDayPriceByTerm(product.getSaleprice(), order.getRentterm(), product.getGenres()));
            } else {
                var rentprice = this.computeRentPriceByDays(product.getDailyrent(), product.getMonthlyrent(), order.getRentdays());
                order.setRentprice(rentprice);
                order.setRentpricebyday(product.getDailyrent());
            }

            //租金超过原价
            if (order.getRentprice().compareTo(allPrice) == 1) {
                order.setRentprice(allPrice);
            }

            o_item.setSumprice(allPrice);
            o_item.setRentcost(product.getSaleprice());


            //租赁周期
            order.setOrderprice(product.getSaleprice());
            order.setPayprice(allPrice);

        }
        //endregion

        //加上 运费
        orderPrice = orderPrice.add(express_price);
        allPrice = allPrice.add(express_price);
        allPrice = allPrice.setScale(2, RoundingMode.HALF_UP);
        order.setOrderprice(orderPrice.setScale(2, RoundingMode.HALF_UP));
        order.setPayprice(allPrice);
        order.setNowprice(allPrice);

        return order;
    }


    /**
     * 支付订单
     *
     * @return
     */
    @Override
    public MyResult payOrder(String wxUserId, PayOrderDTO order) throws WxPayException {
        var cacheKey = String.format(ORDER_KEY, order.getOrderno());
        var expire = this.redisService.getExpire(cacheKey, TimeUnit.SECONDS);
        //限制：如果订单剩余时间不到60秒，则视为无效订单
        if (expire < 60) {
            return MyResult.error("订单已过期，请重新下单!");
        }

        //是否选择了收货地址
        if (StringUtils.isEmpty(order.getReceiveAddressId())) {
            return MyResult.error("请选择收货地址");
        }

        //从缓存中得到订单信息
        var cache = this.redisService.get(cacheKey);
        if (cache != null) {

            var orderInfo = (OrderDTO) cache;
            if (orderInfo.getOrdertype() == OrderEnums.OrderTypeEnum.lease.getValue()) {
                orderInfo.setRentterm(order.getTerm());
            }
            orderInfo.setRentdays(order.getRentdays());
            orderInfo.setRemark(order.getRemark());
            //每次要重新生成订单号，避免同一个订单用户取消支付后再次支付，产生微信商户单重复错误
            orderInfo.setOrderno(GenerateNum.getInstance().GenerateOrder());

            //更新收货地址
            var receiveAddr = this.wxUserAddressService.getById(order.getReceiveAddressId());
            orderInfo.setReceiveaddress(receiveAddr.getAddress());
            orderInfo.setReceivearea(receiveAddr.getArea());
            orderInfo.setReceivecity(receiveAddr.getCity());
            orderInfo.setReceivephoneprefix(receiveAddr.getPhoneprefix());
            orderInfo.setReceivername(receiveAddr.getUsername());
            orderInfo.setReceiverphone(receiveAddr.getPhone());

            //更新快递
            orderInfo.setExpress(order.getExpress());

            //使用的券
            UserCouponDTO useCoupon = null;

            //允许使用券
            if (orderInfo.getCancoupon() == 1) {
                //用户使用了优惠券
                if (StringUtils.isNotEmpty(order.getCouponId())) {
                    //券详情
                    useCoupon = this.userCouponService.getDetailsById(order.getCouponId());
                    //必须是未过期的
                    if (useCoupon.getOverdate().compareTo(DateUtils.now()) != 1) {
                        return MyResult.error("优惠券已过期,请重新选择");
                    }

                }
            }

            var wxUser = this.wxUserService.getById(orderInfo.getWxuserid());

            //非特价秒杀订单
            if (orderInfo.getOrdertype() != seckill.getValue()) {
                //获取最终应付价格
                orderInfo = this.computePayPrice(wxUser, orderInfo, useCoupon, (order.getIsscore() == 1 && orderInfo.getCancoupon() == 1));
            } else {
                //快递费
                if (order.getExpress() == ExpressEnums.Express.zto.getValue()) {
                    orderInfo.setExpressprice(BigDecimal.ZERO);
                } else {
                    //顺风+20元
                    var express_price = BigDecimal.valueOf(20);
                    orderInfo.setExpressprice(express_price);
                    orderInfo.setOrderprice(orderInfo.getOrderprice().add(express_price));

                }
                orderInfo.setPayprice(orderInfo.getOrderprice());
                orderInfo.setNowprice(orderInfo.getOrderprice());
            }

            //放入Redis,存放5min
            this.redisService.set(String.format(ORDER_KEY, orderInfo.getOrderno()), orderInfo, Order_Redis_Timeout, TimeUnit.MINUTES);

            return this.wxUnifiedOrder(orderInfo);

        }
        return MyResult.error("订单已过期，请重新下单!");
    }

    /**
     * 微信统一下单
     *
     * @param orderInfo
     * @return
     * @throws WxPayException
     */
    private MyResult wxUnifiedOrder(OrderDTO orderInfo) throws WxPayException {

        //region 微信统一下单

        //订单有效期5分钟
        var order_starttime = DateUtils.addMinute(DateUtils.now(), 5);
        //支付金额，开发模式下为1分
        BigDecimal payPrice = this.isDev ? BigDecimal.valueOf(0.01) : orderInfo.getPayprice();

        //秒杀的订单通知地址不一样
        var payNotifyUrl = orderInfo.getOrdertype() == seckill.getValue() ? this.secKillOrderNotifyUrl : this.payProperties.getNotifyUrl();

        WxPayUnifiedOrderRequest prepayInfo = WxPayUnifiedOrderRequest.newBuilder()
                .openid(orderInfo.getWxopenid())
                .outTradeNo(orderInfo.getOrderno())
                .totalFee(payPrice.multiply(BigDecimal.valueOf(100)).intValue()) //元-->分
                .body(orderInfo.getOrderno())
                .tradeType("JSAPI")
                .spbillCreateIp("127.0.0.1")
                .notifyUrl(payNotifyUrl) //支付完成通知地址
                .detail("宁心力合数码在线购物")
                .timeStart(DateUtils.formatDate(DateUtils.now(), "yyyyMMddHHmmss"))
                .timeExpire(DateUtils.formatDate(order_starttime, "yyyyMMddHHmmss"))
                .build();


        var result = this.wxPayService.unifiedOrder(prepayInfo);

        //请求成功
        if (result.getResultCode().equalsIgnoreCase(WxPayConstants.ResultCode.SUCCESS) && result.getReturnCode().equalsIgnoreCase(WxPayConstants.ResultCode.SUCCESS)) {
            var map = new HashMap<String, String>();

            map.put("nonceStr", result.getNonceStr());
            map.put("appId", this.payProperties.getAppId());
            map.put("package", "prepay_id=" + result.getPrepayId());
            map.put("signType", "MD5");
            Long timeStamp = System.currentTimeMillis() / 1000;
            map.put("timeStamp", timeStamp + "");//这边要将返回的时间戳转化成字符串，不然小程序端调用wx.requestPayment方法会报签名错误
            //生成签名
            SignUtils signUtils = new SignUtils();
            map.put("paySign", signUtils.createSign(map, "MD5", this.payProperties.getMchKey(), null));

            return MyResult.ok(map);
        } else {
            this.log.error(result.toString());
            return MyResult.error("支付发生异常,请稍后重试");
        }

        //endregion

    }


    /**
     * 微信支付回调通知处理
     *
     * @param data
     * @return
     */
    @Override
    public boolean payNotify(String data) throws WxPayException {
        this.log.info("=====================================处理支付通知======================================");

        var notifyObj = this.wxPayService.parseOrderNotifyResult(data);
        //SUCESS通知
        if (notifyObj.getReturnCode().equalsIgnoreCase(WxPayConstants.ResultCode.SUCCESS)) {
            this.log.info("=====================================微信通知:SUCCESS======================================");

            notifyObj.checkResult(this.wxPayService, "MD5", true);

            this.log.info("=====================================微信通知：验签通过======================================");

            this.rabbitTemplate.convertAndSend(ORDER_SUCCESS, notifyObj, new CorrelationData(IDUtils.genUUID()));

            return true;
        }

        this.log.info("=====================================微信通知:ERROR======================================");
        this.log.error(data);
        this.log.error(notifyObj.toString());

        return false;
    }

    @Override
    public boolean secKillNotify(String data) throws WxPayException {
        this.log.info("=====================================处理支付通知======================================");

        var notifyObj = this.wxPayService.parseOrderNotifyResult(data);
        //SUCESS通知
        if (notifyObj.getReturnCode().equalsIgnoreCase(WxPayConstants.ResultCode.SUCCESS)) {
            this.log.info("=====================================微信通知:SUCCESS======================================");

            notifyObj.checkResult(this.wxPayService, "MD5", true);

            this.log.info("=====================================微信通知：验签通过======================================");

            this.rabbitTemplate.convertAndSend(ORDER_SECKILL_SUCCESS, notifyObj, new CorrelationData(IDUtils.genUUID()));

            return true;
        }

        this.log.info("=====================================微信通知:ERROR======================================");
        this.log.error(data);
        this.log.error(notifyObj.toString());
        return false;
    }


    /**
     * 获取指定时间段的总消费金额
     *
     * @param wxUserId
     * @param start
     * @param end
     * @return
     */
    @Override
    public BigDecimal getSumPriceInTime(String wxUserId, Date start, Date end) {
        return this.baseMapper.getSumPriceInTime(wxUserId, start, end);
    }


    /**
     * 分页获取订单列表，简略显示订单信息
     *
     * @return
     */
    @Override
    public Pagination<OrderMinDTO> getMinListByPage(String wxUserId, PageParameter page, Map<String, Object> filters) {
        //订单的类型
        var type = filters.get("filter");

        Integer[] status = null;
        //排序字段
        var sortField = "addtime";
        if (type.equals("all")) {
            sortField = "addtime desc";
        } else if (type.equals("wait")) {
            status = ObjectUtils.toArray(1);
            sortField = "addtime desc";
        } else if (type.equals("receive")) {
            sortField = "deliverytime desc";
            status = ObjectUtils.toArray(2);
        } else if (type.equals("complete")) {
            sortField = "addtime desc";
            status = ObjectUtils.toArray(3, 4);
        }
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        var result = (Page<TbOrder>) this.baseMapper.getUserOrdersByStatus(status, wxUserId, sortField);

        var pageInfo = new Pagination<OrderMinDTO>(page.getPageIndex(), page.getPageSize(), (int) result.getTotal());
        var ordermins = new ArrayList<OrderMinDTO>();

        var before7Days = DateUtils.subDays(7);
        var before8Days = DateUtils.subDays(8);


        result.getResult().stream().sorted((a, b) -> 0 - a.getAddtime().compareTo(b.getAddtime())).forEachOrdered(e -> {
            var min = this.beanMap(e, OrderMinDTO.class);
            if (e.getOrderItems() == null || e.getOrderItems().size() == 0) {
                return;
            }
            //订单中的购买商品总数
            var sumCount = e.getOrderItems().stream().map(m -> m.getBuycount()).reduce(0, Integer::sum);
            //使用第一个商品作为默认信息
            var firstPro = e.getOrderItems().get(0);
            min.setBuycount(sumCount);
            min.setProductimage(firstPro.getProductimage());
            min.setProductname(firstPro.getProductname());
            min.setProductprice(firstPro.getProductprice());
            min.setCanrefund(1);
            //如果订单只有一个商品，且有退款记录
            if (e.getOrderItems().size() == 1 && !CollectionUtils.isEmpty(e.getRefundItems())) {
                //已完成或者待处理的记录，当前订单项不可再申请退款
                var exists = e.getRefundItems().stream().filter(m -> m.getStatus() != OrderEnums.OrderRefundEnum.allow.getValue()).count();
                if (exists > 0) {
                    min.setCanrefund(0);
                }
                //多个订单项，且有退款记录
            } else if (e.getOrderItems().size() > 1 && !CollectionUtils.isEmpty(e.getRefundItems())) {
                var exists = e.getRefundItems().stream().filter(m -> m.getStatus() != OrderEnums.OrderRefundEnum.allow.getValue()).count();
                //全部申请退款了
                if (exists == e.getOrderItems().size()) {
                    min.setCanrefund(0);
                }
            }

            //订单已关闭了,是否是退款订单
            if (e.getStatus() == OrderEnums.OrderStatusEnum.complete.getValue() && e.getRefund() == OrderEnums.OrderRefundFlagEnum.all.getValue()) {
                min.setSubstatus("已退款");
            }


            //判断订单的状态,订单是已发货，但发货时间超过7天
            if (e.getStatus() == OrderEnums.OrderStatusEnum.transit.getValue() && e.getDeliverytime().compareTo(before7Days) < 1) {
                e.setStatus(OrderEnums.OrderStatusEnum.received.getValue());
                //订单是已收货的，但是收货时间超过8天
            } else if (e.getStatus() == OrderEnums.OrderStatusEnum.received.getValue() && e.getConfirmtime().compareTo(before8Days) < 1) {
                e.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());
            }
            ordermins.add(min);
        });


        pageInfo.setList(ordermins);
        return pageInfo;
    }


    /**
     * 申请退款
     *
     * @return
     */
    @Override
    public MyResult orderRefund(String orderItemId, OrderEnums.OrderRefundReasonEnum reason, String remark, BigDecimal price) {

        var orderItem = this.orderItemService.getById(orderItemId);
        var order = this.getById(orderItem.getOrderid());
        if (order.getOrdertype() == seckill.getValue() || order.getOrdertype() == OrderEnums.OrderTypeEnum.discount.getValue()) {
            if (DateUtils.addMinute(order.getPaytime(), 30).compareTo(new Date()) < 0) {
                return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "特价秒杀订单限30分钟内可退");
            }
        }
        //当前订单项是否有待处理或者已退款的记录
        var hasRefund = this.orderRefundService.count(Example.builder(TbOrderRefund.class)
                .where(
                        Sqls.custom()
                                .andEqualTo("orderitemid", orderItemId)
                                .andIn("status", List.of(OrderEnums.OrderRefundEnum.wait.getValue()
                                        , OrderEnums.OrderRefundEnum.allow.getValue()))).build());
        //该订单不能重复提交申请
        if (hasRefund > 0) {
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "不能重复提交申请");
        }

        var refundRecord = new OrderRefundDTO();
        refundRecord.setOrderid(orderItem.getOrderid());
        refundRecord.setOrderno(orderItem.getOrderno());
        refundRecord.setOrderitemid(orderItemId);
        refundRecord.setRefundcount(orderItem.getBuycount());
        refundRecord.setRefundprice(price);
        refundRecord.setRefundreason(reason.getValue());
        refundRecord.setStatus(OrderEnums.OrderRefundEnum.wait.getValue());
        refundRecord.setThirdorderno(orderItem.getThirdorderno());
        refundRecord.setWxuserid(orderItem.getWxuserid());
        refundRecord.setRefundremark(remark);
        refundRecord.setRefundno(GenerateNum.getInstance().GenerateOrder());
        //当前订单使用了积分抵扣
        if (order.getPayscore().compareTo(BigDecimal.ZERO) == 1) {
            //商品总数
            var sumcount = this.baseMapper.getOrderSumProductCount(orderItem.getOrderid());
            var scoreAvg = order.getPayscore().divide(BigDecimal.valueOf(sumcount), 2, RoundingMode.DOWN).setScale(2, RoundingMode.DOWN);
            var refundScore = scoreAvg.multiply(BigDecimal.valueOf(orderItem.getBuycount()));
            refundRecord.setRefundscore(refundScore);
        }

        var result = this.orderRefundService.add(refundRecord);
        if (result) {
            return MyResult.ok("提交成功");
        }
        return MyResult.error("系统发生异常");
    }


    /**
     * 积分兑换商品
     *
     * @param wxUserId
     * @param productId
     * @return
     */
    @Override
    public MyResult exchangeOrder(String wxUserId, String productId) {

        var productInfo = this.shopService.getShopInfoById(productId);
        //是否是有效商品
        if (productInfo.getIssale() == 1 && productInfo.getStatus() == 1 && productInfo.getIsdelete() == 0) {
            var wxUser = this.wxUserService.getById(wxUserId);
            //用户是否有足够的积分
            var sumScore = wxUser.getVscore();
            //商品售价就是积分
            var product_score = this.computeProductPrice(productInfo, BigDecimal.valueOf(1));
            if (sumScore.compareTo(product_score) > -1) {
                //商品订单关联
                var orderItem = new OrderItemDTO();
                orderItem.setId(IDUtils.genUUID());
                orderItem.setAddtime(new Date());
                orderItem.setProductprice(computeProductPrice(productInfo, null));
                orderItem.setProductimage(productInfo.getThumbnails());
                orderItem.setProductname(productInfo.getShopname());
                orderItem.setProductid(productId);
                orderItem.setTracknumber("");
                orderItem.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());
                orderItem.setIstransited(0);
                orderItem.setExpress(ExpressEnums.Express.zto.getValue());
                orderItem.setBuycount(1);
                orderItem.setWxuserid(wxUserId);
                orderItem.setExchangescore(product_score);
                orderItem.setSumprice(BigDecimal.ZERO);
                orderItem.setProduct(productInfo);


                //创建订单
                OrderDTO newOrder = new OrderDTO();
                newOrder.setId(IDUtils.genUUID());
                newOrder.setAddtime(new Date());
                newOrder.setOrdertype(OrderEnums.OrderTypeEnum.score.getValue());
                newOrder.setWxuserid(wxUserId);
                newOrder.setOrderno(GenerateNum.getInstance().GenerateOrder());
                newOrder.setOrderprice(BigDecimal.ZERO);
                newOrder.setPayprice(BigDecimal.ZERO);
                newOrder.setStatus(OrderEnums.OrderStatusEnum.ready.getValue());

                newOrder.setWxopenid(wxUser.getOpenid());
                newOrder.setRefund(OrderEnums.OrderRefundFlagEnum.none.getValue());
                newOrder.setScore(BigDecimal.ZERO);
                newOrder.setExpress(ExpressEnums.Express.zto.getValue());
                newOrder.setExpressprice(BigDecimal.ZERO);
                newOrder.setIstransited(0);
                newOrder.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());
                newOrder.setExchangescore(product_score);
                newOrder.setPaytime(new Date()); // 支付和积分兑换都算支付时间

                orderItem.setOrderno(newOrder.getOrderno());
                orderItem.setOrderid(newOrder.getId());
                var items = new ArrayList<OrderItemDTO>();
                items.add(orderItem);
                newOrder.setOrderItems(items);

                var receiveAddr = this.wxUserAddressService.getDefaultAddr(wxUserId, true);
                if (receiveAddr != null) {
                    newOrder.setReceiverphone(receiveAddr.getPhone());
                    newOrder.setReceivername(receiveAddr.getUsername());
                    newOrder.setReceivephoneprefix(receiveAddr.getPhoneprefix());
                    newOrder.setReceivecity(receiveAddr.getCity());
                    newOrder.setReceivearea(receiveAddr.getArea());
                    newOrder.setReceiveaddress(receiveAddr.getAddress());
                    newOrder.setReceiveprovince(receiveAddr.getProvince());
                    newOrder.setReceiveAddressId(receiveAddr.getId());
                }

                var cacheKey = String.format(Keys.ORDER_KEY, newOrder.getOrderno());
                this.redisService.set(cacheKey, newOrder, Order_Redis_Timeout, TimeUnit.MINUTES);
                return MyResult.ok(newOrder.getOrderno());

            } else {
                return MyResult.forb("您的积分不足");
            }
        } else {
            return MyResult.forb("无效商品");
        }
    }


    /**
     * 确认兑换订单
     *
     * @param order
     * @return
     * @throws WxPayException
     */
    public MyResult confirmExOrder(PayOrderDTO order) throws WxPayException {

        synchronized (syncScoreLocker) {
            var cacheKey = String.format(Keys.ORDER_KEY, order.getOrderno());
            var cache = this.redisService.get(cacheKey);
            if (cache == null) {
                return MyResult.error("订单已过期，请重新下单!");
            }

            var expire = this.redisService.getExpire(cacheKey, TimeUnit.SECONDS);
            //限制：如果订单剩余时间不到60秒，则视为无效订单
            if (expire < 60) {
                return MyResult.error("订单已过期，请重新下单!");
            }

            var orderInfo = (OrderDTO) cache;

            //是否选择了收货地址
            if (StringUtils.isEmpty(order.getReceiveAddressId())) {
                return MyResult.error("请选择收货地址");
            }
            //用户信息
            var wxUser = this.wxUserService.getById(orderInfo.getWxuserid());

            orderInfo.setRemark(order.getRemark());

            //更新收货地址
            var receiveAddr = this.wxUserAddressService.getById(order.getReceiveAddressId());
            orderInfo.setReceiveaddress(receiveAddr.getAddress());
            orderInfo.setReceivearea(receiveAddr.getArea());
            orderInfo.setReceivecity(receiveAddr.getCity());
            orderInfo.setReceivephoneprefix(receiveAddr.getPhoneprefix());
            orderInfo.setReceivername(receiveAddr.getUsername());
            orderInfo.setReceiverphone(receiveAddr.getPhone());

//        //用户换了快递
//        if (order.getExpress() != orderInfo.getExpress()) {
//            orderInfo.setExpress(order.getExpress());
//            //快递费用
//            var expressPrice = order.getExpress() == ExpressEnums.Express.zto.getValue() ? BigDecimal.ZERO : BigDecdimal.valueOf(20);
//            orderInfo.setExpressprice(expressPrice);
//            orderInfo.setOrderprice(expressPrice);
//            orderInfo.setPayprice(expressPrice);
//            //微信支付
//            return this.wxUnifiedOrder(orderInfo);
//        } else {

            //扣除积分,先用其他平台积分
            var score = wxUser.getVscore().subtract(orderInfo.getExchangescore());
            if (score.compareTo(BigDecimal.ZERO) == -1) {
                return MyResult.forb("积分不足");
            }
            wxUser.setVscore(score);
            var result = this.transactionUtils.transact((a) -> {
                //直接入库,不需要微信支付
                this.add(orderInfo);
                this.orderItemService.addBatch(orderInfo.getOrderItems());
                //更新用户
                this.wxUserService.updateById(wxUser);
                //清除当前订单缓存
                this.redisService.remove(cacheKey);
                //同时更新redis中的用户信息
                if (this.redisService.exists(wxUser.getSessiontoken())) {
                    wxUser.setNickname(this.emojiConverter.toAlias(wxUser.getNickname()));
                    this.redisService.set(wxUser.getSessiontoken(), wxUser, userRedisExprie, TimeUnit.DAYS);
                }
            });
            if (result) {
                //不用关心，成功与否,通知就好
                var res = SendPost.post(this.webshopNotify + "/api/web/user/sync", JsonUtils.objectToJson(wxUser), MessageModel.class);
                return MyResult.build(201, "下单成功");
            }
        }
        return MyResult.error("系统发生异常");
    }


    /**
     * 按条件查询
     *
     * @param page    分页
     * @param filters 条件
     * @param sorts   排序字段
     * @param isAsc   排序
     * @return
     */
    @Override
    public Pagination<OrderDTO> query(PageParameter page, Map<String, Object> filters, String[] sorts, Integer[] isAsc) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());

        String filter = filters.get("filter") != null ? "%" + filters.get("filter").toString() + "%" : null;

        Integer type;
        Integer ordertype = null;
        switch (filters.get("type").toString()) {
            //全部订单
            case "all":
                type = null;
                break;
            //待发货订单
            case "wait":
                type = 1;
                break;
            //待退款订单
            case "refund":
                type = 5;
                break;
            //待收货订单
            case "receive":
                type = 2;
                break;
            //秒杀订单
            case "seckill":
                ordertype = 5;
                type = 4;
                break;
//            //秒杀订单 未曾使用，暂时保留
//            case "seckillwait":
//                ordertype = 5;
//                type = 4;
//                break;
            //出租订单
            case "lease":
                ordertype = 4;
                type = null;
                break;
            default:
                type = null;
                break;
        }
        List<TbOrder> tbOrders = new ArrayList<>();

        if (("%消费订单%").equals(filter)) {
            ordertype = 1;
        } else if (("%积分订单%").equals(filter)) {
            ordertype = 2;
        } else if (("%租赁订单%").equals(filter)) {
            ordertype = 4;
        } else if (("%秒杀订单%").equals(filter)) {
            ordertype = 5;
        }
        if ((filters.get("type").toString()).equals("refund")) {
            tbOrders = this.baseMapper.queryByRefundType(filter, type, ordertype);
        } else {
            tbOrders = this.baseMapper.queryByFilter(filter, type, ordertype);
        }

        PageInfo<TbOrder> pageInfo = new PageInfo<>(tbOrders);
//        var dtos = this.beanListMap(pageInfo.getList(), this.currentDTOClass());
//        var dtoPage = this.beanMap(pageInfo, PageInfo.class);
//        dtoPage.setList(dtos);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }


    /**
     * 创建excel表格
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws IOException
     */
    @Override
    public List<HashMap<String, Object>> createExcelByOrder(Date startDate, Date endDate) {
//        Date date = DateUtils.subDays(5);
        String startDateStr = DateUtils.getTimeStampStr(startDate);
        String endDateStr = DateUtils.getTimeStampStr(endDate);
        List<TbOrder> allDetails = this.baseMapper.getAllDetails(startDateStr, endDateStr);

//        HashMap<String, Object> row = new HashMap<>();
        List<HashMap<String, Object>> listRow = new ArrayList<>();
        allDetails.forEach(order -> {
            order.getOrderItems().forEach(orderitem -> {
                HashMap<String, Object> row = new HashMap<>();
                row.put("orderno", order.getOrderno());//订单号
                row.put("thirdorderno", orderitem.getThirdorderno());//物流单号
                row.put("productname", orderitem.getProductname());//商品名称
                row.put("productprice", orderitem.getProductprice().doubleValue());//商品金额
                BigDecimal bigDecimal = sumPayPrice(order, orderitem);
                row.put("sumprice", bigDecimal.doubleValue());//合计金额
                row.put("buycount", orderitem.getBuycount());//数量
                row.put("addtime", order.getAddtime());//下单日期
                row.put("payprice", order.getPayprice());//用户支付金额
                row.put("expressprice", order.getExpressprice());//快递费用
                row.put("receiveprovince", order.getReceiveprovince());//省
                row.put("receivecity", order.getReceivecity());//市
                row.put("receivearea", order.getReceivearea());//区
                row.put("receiveaddress", order.getReceiveaddress());//详细收货地址
                row.put(" remark", order.getRemark());//备注
                row.put("deliverytime", orderitem.getDeliverytime());//发货日期
                row.put("confirmtime", orderitem.getConfirmtime());//收货日期
                row.put("buycount", orderitem.getBuycount());//购买数量
                row.put("productId", orderitem.getProductid());//购买数量
                row.put("ordertype", OrderEnums.OrderTypeEnum.getMessage(order.getOrdertype()));//订单类型
                row.put("status", OrderEnums.OrderStatusEnum.getMessage(order.getStatus()));//订单状态
                row.put("nickname", order.getWxuser() != null ? order.getWxuser().getNickname() : "");//微信昵称
                row.put("phone", order.getWxuser() != null ? order.getWxuser().getPhone() != null ? order.getWxuser().getPhone() : "" : "");//微信手机号
                row.put("receivername", order.getReceivername() != null ? order.getReceivername() : "");//收件人姓名
                row.put("receiverphone", order.getReceiverphone() != null ? order.getReceiverphone() : "");//收件人手机号
                String address = order.getReceiveprovince() + order.getReceivecity() + order.getReceivearea() + '-' + order.getReceiveaddress();
                row.put("address", address);//收件人地址
                List<TbOrderRefund> collect = order.getRefundItems().stream().filter(e -> e.getOrderitemid().equals(orderitem.getId()) && e.getStatus() == OrderEnums.OrderRefundEnum.allow.getValue()).collect(toList());
                double refundprice = 0.0;
                if (collect.size() > 0) {
                    refundprice = collect.get(0).getRefundprice().doubleValue();
                }
                row.put("refundPrice", refundprice);//退款金额
                listRow.add(row);
            });
        });
        return listRow;
    }

    public BigDecimal sumPayPrice(TbOrder orderDTO, OrderItemDTO orderItemDTO) {
        BigDecimal orderPrice = orderDTO.getOrderprice().subtract(orderDTO.getExpressprice());
        //商品价格在总价格中的占比
        BigDecimal divide = orderItemDTO.getSumprice().divide(orderPrice, 2, RoundingMode.DOWN);
        //是否使用优惠券
        if (orderDTO.getCheap().doubleValue() > 0.0) {
            //当前商品的优惠金额
            BigDecimal multiply = divide.multiply(orderDTO.getCheap());
            BigDecimal price = orderItemDTO.getSumprice().subtract(multiply);
            return price;
        } else if (orderDTO.getPayscore().doubleValue() > 0.0) { //是否使用积分
            //当前商品的优惠金额
            BigDecimal multiply = divide.multiply(orderDTO.getPayscore());
            BigDecimal price = orderItemDTO.getSumprice().subtract(multiply);
            return price;
        }

        return orderItemDTO.getSumprice();
    }

    /**
     * 确认收货
     *
     * @param orderid
     * @return
     */
    @Override
    public MyResult confirmReceive(String orderid, OrderEnums.OrderConfirmTypeEnum confirmTypeEnum) {

        var order = this.getDetails(orderid);


        var now = new Date();

        //订单状态为待收货
        if (order.getStatus() == OrderEnums.OrderStatusEnum.transit.getValue()) {
            //更新订单状态
            order.setStatus(OrderEnums.OrderStatusEnum.received.getValue());
            order.setConfirmtime(now);
            order.setConfirmtype(confirmTypeEnum.getValue());

            //订单对应的商品状态也更新
            order.getOrderItems().forEach(e -> {
                if (e.getStatus() == OrderEnums.OrderStatusEnum.transit.getValue()) {
                    e.setStatus(OrderEnums.OrderStatusEnum.received.getValue());
                    e.setConfirmtime(now); //收货时间
                    e.setConfirmtype(confirmTypeEnum.getValue());
                }
            });

            WxUserRentShopDTO[] rents = new WxUserRentShopDTO[1];
            if (order.getOrdertype() == OrderEnums.OrderTypeEnum.lease.getValue()) {
                rents[0] = this.wxUserRentShopService.getByOrderId(order.getId());
                rents[0].setEnddate(DateUtils.addDay(new Date(), order.getRentdays()));
            }

            var result = this.transactionUtils.transact((a) -> {
                if (rents[0] != null) {
                    this.wxUserRentShopService.updateById(rents[0]);
                }
                this.updateById(order);
                this.wxUserService.updateById(order.getWxuser());
                this.orderItemService.updateBatchById(order.getOrderItems());

            });
            if (result) {
                return MyResult.ok();
            }
            throw new RuntimeException("系统异常");


        } else {
            return MyResult.error("订单状态无效,操作失败!");
        }

    }

    private Integer getVipLevel(String wxUserId, BigDecimal exp) {
        var now = new Date();


        //一万积分以上，金牌
        if (exp.compareTo(BigDecimal.valueOf(10000)) > -1) {
            return VIPEnums.VIPTypeEnum.Gold.getValue();
            //5000-9999银牌
        } else if (exp.compareTo(BigDecimal.valueOf(5000)) > -1) {
            return VIPEnums.VIPTypeEnum.Silver.getValue();
            //500-4999铜牌
        } else if (exp.compareTo(BigDecimal.valueOf(500)) > -1) {
            return VIPEnums.VIPTypeEnum.Bornze.getValue();
        } else {
            //普通用户

            var map = new HashMap<Date, Date>();

            for (int i = -2; i < 0; i++) {
                var month3ago = DateUtils.addMonths(new Date(), i);

                //3个月中的当前月的第一天
                var startDate = DateUtils.getFirstDateOfMonth(month3ago);
                //次月的第一天
                var endDate = DateUtils.getFirstDateOfMonth(DateUtils.addMonths(month3ago, 1));

                map.put(startDate, endDate);

            }
            var count = this.baseMapper.hasFewMonthOrder(map, wxUserId);
            if (count == 3) {
                return VIPEnums.VIPTypeEnum.Bornze.getValue();
            }


            return VIPEnums.VIPTypeEnum.None.getValue();
        }

    }

    /**
     * 计算软件租赁价格，根据租赁周期
     *
     * @return
     * @temr 周期
     * @type 商品类型 1软件  2硬件
     */
    private BigDecimal computeRentPriceByTerm(BigDecimal price, int term) {
        BigDecimal finalPrice = BigDecimal.ZERO;

        switch (term) {
            case 1: //月
                finalPrice = computeRentDayPriceByTerm(price, term, 1).multiply(BigDecimal.valueOf(30)).setScale(0, RoundingMode.DOWN);
                break;
            case 2: //季度
                finalPrice = computeRentDayPriceByTerm(price, term, 1).multiply(BigDecimal.valueOf(90)).setScale(0, RoundingMode.DOWN);
                break;
            case 3: //半年
                finalPrice = computeRentDayPriceByTerm(price, term, 1).multiply(BigDecimal.valueOf(180)).setScale(0, RoundingMode.DOWN);
                break;
            case 4:  //年
                finalPrice = computeRentDayPriceByTerm(price, term, 1).multiply(BigDecimal.valueOf(365)).setScale(0, RoundingMode.DOWN);
                break;
        }
        return BigDecimal.valueOf(finalPrice.intValue());
    }

    /**
     * 计算硬件租赁价
     *
     * @return
     */
    private BigDecimal computeRentPriceByDays(BigDecimal dayprice, BigDecimal monthprice, int days) {
        BigDecimal finalPrice = BigDecimal.ZERO;

        var monthes = days < 30 ? 0 : days / 30;
        var dates = days - monthes * 30;

        finalPrice = monthprice.multiply(BigDecimal.valueOf(monthes)).add(dayprice.multiply(BigDecimal.valueOf(dates))).setScale(0, RoundingMode.DOWN);
        return BigDecimal.valueOf(finalPrice.intValue());
    }


    private BigDecimal computeRentDayPriceByTerm(BigDecimal price, int term, int type) {
        if (type == 1) {
            switch (term) {
                case 1: //月
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(3)).setScale(2, RoundingMode.DOWN);
                case 2: //季度
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(1.5)).setScale(2, RoundingMode.DOWN);
                case 3: //半年
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(1)).setScale(2, RoundingMode.DOWN);
                case 4:  //年
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.8)).setScale(2, RoundingMode.DOWN);
            }
        } else {
            switch (term) {
                case 2: //季度
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(1.5)).setScale(2, RoundingMode.DOWN);
                case 3: //半年
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(1.2)).setScale(2, RoundingMode.DOWN);
                case 4:  //年
                    return price.divide(BigDecimal.valueOf(365), 2, RoundingMode.DOWN).multiply(BigDecimal.valueOf(0.9)).setScale(2, RoundingMode.DOWN);
            }
        }
        return BigDecimal.ZERO;
    }


    /**
     * 租赁商品
     *
     * @return
     */
    @Override
    public MyResult rentOrder(String wxUserId, String productId) {
        var user = this.wxUserService.getById(wxUserId);
        var product = this.shopService.getShopInfoById(productId);
        var term = 1;
        //硬件类，从季度开始
//        if (product.getGenres() == 2) {
//            term = 2;
//
//        }
        //是否有当前正在租赁的商品
//        var rentRecord = this.wxUserRentShopService.isExistValidRentByWxUserId(wxUserId);
//        if (rentRecord != null) {
//            return MyResult.forb("租赁失败,您当前存在正在租赁的商品");
//        }

//        if (count > 0) {
//            return MyResult.forb("租赁失败，30天内只能租赁一次!");
//        }


//        if (user.getVipid() == VIPEnums.VIPTypeEnum.Gold.getValue()) {
        //商品必须可租
        if (product.getIsrent() == 1) {

            final var order = new OrderDTO();
            order.setWxopenid(user.getOpenid());
            order.setWxuserid(wxUserId);
            order.setExpress(ExpressEnums.Express.zto.getValue());
            order.setRefund(OrderEnums.OrderRefundFlagEnum.none.getValue());
            order.setOrdertype(OrderEnums.OrderTypeEnum.lease.getValue());
            order.setOrderno(GenerateNum.getInstance().GenerateOrder());
            order.setId(IDUtils.genUUID());
            order.setAddtime(new Date());
            order.setCoupontype(CouponEnums.CouponTypeEnum.None.getValue());
            order.setCancoupon(0);

            var o_item = new OrderItemDTO();
            o_item.setProduct(product);
            o_item.setId(IDUtils.genUUID());
            o_item.setAddtime(new Date());
            o_item.setBuycount(1);
            o_item.setExpress(order.getExpress());
            o_item.setIstransited(0);
            o_item.setOrderid(order.getId());
            o_item.setOrderno(order.getOrderno());
            o_item.setProductid(product.getId());
            o_item.setProductname(product.getShopname());
            o_item.setProductimage(product.getThumbnails());
            o_item.setWxuserid(wxUserId);

            o_item.setProductprice(product.getSaleprice());
            o_item.setStatus(OrderEnums.OrderStatusEnum.nopay.getValue());
            o_item.setTracknumber("");
            o_item.setProducttype(product.getGenres());

            //直接支付商品原价
            BigDecimal allPrice = product.getSaleprice();
//            if (user.getVipid() == VIPEnums.VIPTypeEnum.Gold.getValue()) {
////                var startDate = DateUtils.addDay(new Date(), -30);
////                //一个月内的游戏租数
////                var count = this.wxUserRentShopService.getRentRecordInDateTime(startDate, wxUserId, 1);
////                if (count > 0) {
////                    allPrice = this.computeRentPriceByTerm(product.getSaleprice(), term, product.getGenres()).add(product.getSaleprice());
////                } else {
////                    order.setFreecost(true);
////                    allPrice = this.computeRentPriceByTerm(product.getSaleprice(), term, product.getGenres());
////                }
////            } else {
////                allPrice = this.computeRentPriceByTerm(product.getSaleprice(), term, product.getGenres()).add(product.getSaleprice());
////            }
            //软件按公式
            if (product.getGenres() == 1) {
                o_item.setRentprice(this.computeRentPriceByTerm(product.getSaleprice(), term));
                //每日租金
                order.setRentpricebyday(this.computeRentDayPriceByTerm(product.getSaleprice(), term, product.getGenres()));
                //租赁周期
                order.setRentterm(term);
                order.setRentprice(o_item.getRentprice());
            } else {
                //硬件按天数算,默认7日
                o_item.setRentprice(this.computeRentPriceByDays(product.getDailyrent(), product.getMonthlyrent(), 7));
                //每日租金
                order.setRentpricebyday(product.getDailyrent());
                order.setRentdays(7);
                order.setRentprice(o_item.getRentprice());
            }

            //租金超过原价
            if (order.getRentprice().compareTo(allPrice) == 1) {
                order.setRentprice(allPrice);
            }
            o_item.setSumprice(allPrice);

            order.setOrderprice(allPrice);
            order.setPayprice(allPrice);

            var items = new ArrayList<OrderItemDTO>();
            items.add(o_item);
            //关联
            order.setOrderItems(items);


            //用户的收货地址
            var address = this.wxUserAddressService.getDefaultAddr(wxUserId, true);
            //新用户可能没有填写收货地址
            if (address != null) {
                order.setReceiveaddress(address.getAddress());
                order.setReceivearea(address.getArea());
                order.setReceivecity(address.getCity());
                order.setReceivephoneprefix(address.getPhoneprefix());
                order.setReceivername(address.getUsername());
                order.setReceiverphone(address.getPhone());
                order.setReceiveAddressId(address.getId());
            }

            order.setOrderprice(allPrice);
            //放入Redis,存放5min
            this.redisService.set(String.format(ORDER_KEY, order.getOrderno()), order, Order_Redis_Timeout, TimeUnit.MINUTES);
            return MyResult.ok(order.getOrderno());


        }
        return MyResult.error("非可租赁商品");
//        }
//        return MyResult.error("非金牌会员，不可出租!");
    }

    @Override
    public MyResult refundRentOrder(String orderId, BigDecimal refundprice) throws WxPayException {
        var order = this.getDetails(orderId);
        //是否是出租订单
        if (order.getOrdertype() == OrderEnums.OrderTypeEnum.lease.getValue()) {
            //该订单是否已退款
            if (order.getRefund() == OrderEnums.OrderRefundFlagEnum.none.getValue()) {

                //30天内允许退,以收货时间为准
//                if (order.getConfirmtime() == null || DateUtils.daysBetween(order.getConfirmtime(), new Date()) <= 30) {

                //退款单号
                var refundNo = GenerateNum.getInstance().GenerateOrder();
                WxPayRefundResult refundResult = null;
                //如果没有退款金额或不需要退款
                if (refundprice != null && refundprice.compareTo(BigDecimal.ZERO) == 0) {
                    refundprice = BigDecimal.ZERO;

                    this.log.debug("==========================开始退款=========================");

                    WxPayRefundRequest request = WxPayRefundRequest.newBuilder()
                            .transactionId(order.getThirdorderno())
                            .notifyUrl(this.refunNotifyUrl)
                            .totalFee(refundprice.multiply(BigDecimal.valueOf(100)).intValue()) //转分
                            .outRefundNo(refundNo)
                            .refundFee(order.getPayprice().subtract(order.getExpressprice()).multiply(BigDecimal.valueOf(100)).intValue()) //全额退款
                            .build();

                    refundResult = this.wxPayService.refund(request);
                    if (refundResult.getReturnCode().equals("SUCCESS")) {
                        this.log.debug("==========================退款成功=========================");

                    } else {
                        return MyResult.error("退款失败:" + refundResult.getReturnMsg());

                    }
                }

                order.setRefund(OrderEnums.OrderRefundFlagEnum.all.getValue());
                order.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());

                var orderItem = order.getOrderItems().get(0);
                orderItem.setStatus(OrderEnums.OrderStatusEnum.complete.getValue());

                var orderRefund = order.getRefundItems().get(0);
                orderRefund.setId(IDUtils.genUUID());
                orderRefund.setAddtime(new Date());
                orderRefund.setRefundscore(BigDecimal.ZERO);
                orderRefund.setRefundno(refundNo);
                orderRefund.setStatus(OrderEnums.OrderRefundEnum.allow.getValue());
                orderRefund.setOrderid(order.getId());
                orderRefund.setOrderno(order.getOrderno());
                orderRefund.setRefundcount(1);
                orderRefund.setOrderitemid(order.getOrderItems().get(0).getId());
                orderRefund.setRefundprice(refundprice);
                orderRefund.setRefundreason(OrderEnums.OrderRefundReasonEnum.reason7.getValue());
                orderRefund.setThirdorderno(order.getThirdorderno());
                orderRefund.setVerifytime(new Date());
                orderRefund.setWxuserid(order.getWxuserid());
                //押金
                orderRefund.setRentcost(order.getOrderItems().get(0).getRentcost());
                if (refundResult != null) {
                    orderRefund.setThirdrefundno(refundResult.getRefundId());
                }

                //租赁记录
                var rentRecord = wxUserRentShopService.getByOrderId(orderId);
                rentRecord.setStatus(OrderEnums.RentOrderStatusEnum.complete.getValue());
                rentRecord.setBacktime(new Date());
                if (order.getConfirmtime() != null) {
                    //到期日
                    var endDateStr = DateUtils.toString(DateUtils.addDay(order.getConfirmtime(), order.getRentdays()), DateUtils.DATE_FORMAT_DATEONLY);
                    if (DateUtils.toString(new Date(), DateUtils.DATE_FORMAT_DATEONLY).compareTo(endDateStr) == 1) { //逾期了
                        var days = DateUtils.daysBetween(new Date(), order.getConfirmtime());
                        days = days == 0 ? 1 : days; //最小1天
                        rentRecord.setOverduedays((int) days);
                        rentRecord.setOverdueprice(BigDecimal.valueOf(days).multiply(order.getRentpricebyday()));
                    }
                }

                var result = this.transactionUtils.transact((a) -> {
                    this.orderRefundService.add(this.beanMap(orderRefund, OrderRefundDTO.class));
                    this.updateById(order);
                    this.orderItemService.updateById(orderItem);
                    this.wxUserRentShopService.updateById(rentRecord);
                });
                if (result) {
                    return MyResult.ok("归还成功");
                } else {
                    return MyResult.forb("归还失败");
                }

//                }
//                return MyResult.forb("该订单已超过租赁有效期,不可退款!");
            }
            return MyResult.forb("该订单已退款");
        }
        return MyResult.forb("无效操作");
    }

    @Override
    public Map<String, Long> getCountGroupByStatus(String wxUserId) {
        var result = this.baseMapper.getCountGroupByOrderStatus(wxUserId);
        var map = new HashMap<String, Long>();
        result.forEach(e -> {
            var s = e.get("status").toString();
            var status = Integer.parseInt(s);
            var count = e.get("count");
            switch (status) {
                case 1:
                    map.put("wait", count);
                    break;
                case 2:
                    map.put("ready", count);
                    break;
                case 3:
                    map.put("received", count);
                    break;
                case 4:
                    map.put("complete", count);
                    break;

            }
        });
        return map;
    }

    @Override
    public Boolean hasFewMonthOrder(Map<Date, Date> dates, String wxUserId) {
        return this.baseMapper.hasFewMonthOrder(dates, wxUserId) == dates.size();
    }

    @Override
    public Boolean checkMSStock(String wxUserId, String sid, String productId) {

        var wxUser = this.wxUserService.getById(wxUserId);
        var seckill = (SeckillDTO) this.redisService.get(String.format(MS_SECKILL_KEY, sid));
        //如果是会员活动，用户非会员
        if (seckill == null || seckill.getStatus() == 0 || (seckill.getUsertype() == 1 && wxUser.getVipid() == 0)) {
            return false;
        }

        //所有的秒杀商品
        var ms_products = (List<ShopDTO>) this.redisService.get(String.format(MS_PRODUCTS_KEY, sid));
        if (CollectionUtils.isEmpty(ms_products)) {
            return false;
        }

        //秒杀商品的库存占-->商品ID:每笔订单的占用到期时间(List)
        Map<String, Map<String, Date>> ms_order_stocks = null;
        //是否有秒杀商品的记录
        if (this.redisService.exists(String.format(MS_SHOP_STOCK_SET_KEY, sid))) {
            ms_order_stocks = (Map<String, Map<String, Date>>) this.redisService.get(String.format(MS_SHOP_STOCK_SET_KEY, sid));
        } else {
            ms_order_stocks = new HashMap<>();
        }

        //商品列表中是否还有当前商品，没有则跳过
        var product_opt = ms_products.stream().filter(e -> e.getId().equalsIgnoreCase(productId)).findFirst();
        ShopDTO product = null;
        if (product_opt.isPresent()) {
            product = product_opt.get();
        } else {
            return false;
        }

        if (product.getVipShop().getStockcount() == 0) {
            return false;
        }

        //检查当前商品的库存占用
        var stocks = ms_order_stocks.get(productId);
        if (!CollectionUtils.isEmpty(stocks)) {
            var now = DateUtils.now();
            //过滤掉无效的订单占用
//            stocks = stocks.values().stream().filter(e -> now.before(e)).collect(toList());
            var validStocks = new HashMap<String, Date>();
            stocks.entrySet().forEach(e -> {
                if (now.before(e.getValue())) {
                    validStocks.put(e.getKey(), e.getValue());
                }
            });
            stocks = validStocks;
            ms_order_stocks.put(productId, stocks);
            this.redisService.set(String.format(MS_SHOP_STOCK_SET_KEY, sid), ms_order_stocks);

        } else {
            stocks = new HashMap<String, Date>();
        }

        //当前商品库存全部被占用了，则不能购买
        //可能存在，库存占用比实际库存多的情况，因为老的库存占用没有清理掉，实际库存又更新了
        if (stocks.size() >= product.getVipShop().getStockcount()) {
            return false;
        }


        return true;

    }


    /**
     * 根据订单id获取退款订单
     *
     * @param id
     * @return
     */
    @Override
    public MyResult getRefundByOrderId(String id) {
        List<OrderRefundDTO> refundById = this.orderRefundService.getRefundById(id);
        return MyResult.ok(refundById);
    }

    /**
     * 提醒用户出租商品即将到期
     *
     * @return
     */
    @Override
    public boolean remindLeaseExpire() throws ClientException {
        return this.transactionUtils.transact((a) -> {

            //region 给所有收获的出租订单计算日期
            List<WxUserRentShopDTO> endDateIsNullOrder = this.wxUserRentShopService.getEndDateIsNullOrder();
            for (int i = 0; i < endDateIsNullOrder.size(); i++) {
                var orderItemDTO = endDateIsNullOrder.get(i).getOrderItemDTO();
                if (orderItemDTO.getStatus().equals(OrderEnums.OrderStatusEnum.received)) {
                    Date date = DateUtils.addDay(orderItemDTO.getConfirmtime(), endDateIsNullOrder.get(i).getOrder().getRentdays());
                    endDateIsNullOrder.get(i).setEnddate(date);
                    this.wxUserRentShopService.updateById(endDateIsNullOrder.get(i));
                }
            }
            //endregion
            //region 给还剩三天结束的租赁订单发送短信体系
            Date startDate = new Date();
            Date endDate = DateUtils.addDay(startDate, 3);
            var orderList = this.baseMapper.getRentRecordByEndDate(startDate, endDate);
            if (!CollectionUtils.isEmpty(orderList)) {
                DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
                IAcsClient acsClient = new DefaultAcsClient(profile);
                orderList.forEach(order -> {
                    if (order.getEnddate() != null) {
                        CommonRequest request = new NxlhCommonRequest("SMS_164155446");
                        Calendar c = Calendar.getInstance();
                        c.setTime(order.getEnddate());

                        request.putQueryParameter("PhoneNumbers", order.getReceiverphone());
                        //Calendar月份从0开始算，所以这边加1
                        request.putQueryParameter("TemplateParam", String.format("{\"name\":\"%s\",\"y\":\"%s\",\"m\":\"%s\",\"d\":\"%s\"}", order.getReceivername(), c.get(Calendar.YEAR), c.get(Calendar.MONTH) + 1, c.get(Calendar.DAY_OF_MONTH)));

                        try {
                            System.out.println("-----------------------" + order.getOrderno() + "发送短信------------------------");
                            CommonResponse response = acsClient.getCommonResponse(request);
                            System.out.println(response.getData());
                            System.out.println("-----------------------" + order.getOrderno() + "发送结束------------------------");

                        } catch (ServerException e) {
                            e.printStackTrace();
                        } catch (ClientException e) {
                            e.printStackTrace();
                        }
                    }
                });

            }
            //endregion

            //region 判断租赁订单的逾期金额是否超过押金，超过自动买断
            //获取用户逾期租赁
            var wxUserRentShopService = this.wxUserRentShopService.getOverdue(startDate);
            if (!CollectionUtils.isEmpty(wxUserRentShopService)) {
                wxUserRentShopService.stream().forEach(item -> {
                    var days = DateUtils.daysBetween(new Date(), item.getOrder().getConfirmtime());
                    item.setOverduedays((int) days);
                    //逾期算3倍每日租金
                    var price = BigDecimal.valueOf(days).multiply(item.getOrder().getRentpricebyday()).multiply(new BigDecimal(3));
                    //押金
                    var deposit = item.getOrderItemDTO().getRentcost().subtract(item.getOrderItemDTO().getRentprice());
                    item.setOverdueprice(price);
                    //如果押金大于逾期金额自动买断
                    if (price.compareTo(deposit) != -1) {
                        item.setStatus(3);
                        var order = item.getOrder();
                        order.setStatus(4);
                        order.setClosetime(startDate);
                        var orderItem = item.getOrderItemDTO();
                        orderItem.setStatus(4);
                        orderItem.setClosetime(startDate);
                        this.baseMapper.updateByPrimaryKey(order);
                        this.orderItemService.updateById(orderItem);
                    }
                    this.wxUserRentShopService.updateById(item);
                });
            }

            //endregion


        });
    }

    @Override
    public boolean closeWebOrder(List<Long> ids) {

        List<WebOrderDTO> orders = webOrderService.getOverOrderByIds(ids);
        var wxusers = orders.stream().map(o -> o.getWxuser()).distinct().collect(toList());
        var now = DateUtils.now();
        orders.forEach(order -> {
            //会员用户,算积分
            BigDecimal score = BigDecimal.ZERO;
            var user = wxusers.stream().filter(e -> e.getUnionid().equalsIgnoreCase(order.getUnionid())).findFirst().get();
            var payPrice = order.getItempayprice();//不算快递费

            switch (user.getVipid()) {
                case 1:
                    score = payPrice.divide(BigDecimal.valueOf(100));
                    break;
                case 2:
                    score = payPrice.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(1.5));
                    break;
                case 3:
                    score = payPrice.divide(BigDecimal.valueOf(100)).multiply(BigDecimal.valueOf(2));
                    break;
                default:
                    break;
            }
            //判断用户原先是否经验值
            if (user.getExp() == null) {
                user.setExp(BigDecimal.ZERO);
            }
            BigDecimal reduce = BigDecimal.ZERO;
            //普通订单并且子订单数大于1且订单有过退款
            if (order.getOrdertype2().equals(OrderEnums.OrderTypeEnum.sale.getValue()) && order.getProducttype() == 1) {

                //获取订单中已完成的软件订单
                reduce = order.getItempayprice().multiply(new BigDecimal(2));

            } else {
                //获取订单中已完成的硬件订单
                reduce = order.getItempayprice();
            }
            BigDecimal productprice = reduce;
            user.setExp(user.getExp().add(productprice));
            //累计消费
            user.setSumpay(payPrice.add(user.getSumpay()));

            //非普通用户
            if (user.getVipid() != VIPEnums.VIPTypeEnum.None.getValue()) {
                //更新用户积分
                user.setVscore(score.add(user.getVscore()));
            }
        });
        var result = this.transactionUtils.transact((a) -> {
            Boolean aBoolean = webOrderService.CloseByOrderIds(ids);

            //最后根据用户的经验值决定会员等级
            wxusers.forEach(e -> {
                //检查用户会员等级，根据累计消费情况更新
                var level = this.getVipLevel(e.getId(), e.getExp());
                if (e.getVipid() < level) {
                    e.setVipid(level);
                }
            });
            var res = this.wxUserService.updateBatchById(wxusers);
            //更新缓存中的用户信息
            wxusers.forEach(e -> {
                if (this.redisService.exists(e.getSessiontoken())) {

                    //fix emoji
                    e.setNickname(this.emojiConverter.toAlias(e.getNickname()));
                    this.redisService.set(e.getSessiontoken(), e, userRedisExprie, TimeUnit.DAYS);
                }
            });


        });
        if (result) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    sendWebUser(wxusers, "weboverorder");
                }
            }).start();

        }
        return result;
//        return false;
    }

    /**
     * 往网站发送数据
     *
     * @param wxUserDTOS
     */
    private void sendWebUser(List<WxUserDTO> wxUserDTOS, String keys) {

        System.out.println("sendWebUser");
        List<UserTO> userDTOS = new ArrayList<>();
        for (WxUserDTO e : wxUserDTOS) {
            UserTO userTO = new UserTO();
            userTO.setExp(e.getExp());
            userTO.setSumpay(e.getSumpay());
            userTO.setUnionId(e.getUnionid());
            userTO.setVipType(e.getVipid());
            userDTOS.add(userTO);
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sDate = simpleDateFormat.format(new Date());
        Date date = null;
        try {
            date = simpleDateFormat.parse(sDate);
        } catch (ParseException e) {
            log.debug("Get Current Date Exception [by ninja.hzw]" + e);
        }
        int indexMap = 0;
        Map<Integer, List<UserTO>> map = new HashMap<>();
        int size = (userDTOS.size()) % 100 == 0 ? ((userDTOS.size()) / 100) : ((userDTOS.size()) / 100) + 1;
        if (size == 0) size++;
        for (int i = 1; i <= size; i++) {
            var index = i * 100;
            indexMap++;
            if (i == size) {
                List<UserTO> dto = userDTOS.subList(index - 100, userDTOS.size());
                map.put(indexMap, userDTOS.subList(index - 100, userDTOS.size()));
                String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + indexMap + "\"";
                redisService.set(key, dto, 86400l);
            } else {
                List<UserTO> dto = userDTOS.subList(index - 100, index - 1);
                map.put(indexMap, userDTOS.subList(index - 100, index - 1));
                String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + indexMap + "\"";
                redisService.set(key, dto, 86400l);
            }
        }
        for (Map.Entry<Integer, List<UserTO>> entry : map.entrySet()) {
            String key = "$\"NXLH.WX.ADMIN:sendWebUser_" + date + "_" + keys + "_" + entry.getKey() + "\"";
            String data = JsonUtils.objectToJson(entry.getValue());
            System.out.println("发送消息");
            var res = SendPost.sendPostMsg(checkVipIsInvalidationUrl, data);
            if (res) {
                redisService.remove(key);
            }
        }
        System.out.println("结束");

    }
}
