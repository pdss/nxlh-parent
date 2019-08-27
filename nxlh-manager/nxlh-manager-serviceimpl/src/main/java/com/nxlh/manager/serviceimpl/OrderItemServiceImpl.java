package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbOrderItemMapper;
import com.nxlh.manager.model.dbo.TbOrderItem;
import com.nxlh.manager.model.dto.OrderItemDTO;
import com.nxlh.manager.model.enums.CouponEnums;
import com.nxlh.manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service(interfaceClass = OrderItemService.class)
public class OrderItemServiceImpl extends BaseDbCURDSServiceImpl<TbOrderItemMapper, TbOrderItem, OrderItemDTO> implements OrderItemService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserCouponService userCouponService;

    @Autowired
    private CouponsService couponsService;

    @Override
    public MyResult getOrderItemDetail(String id) {
        //该订单项的信息
        var item = this.baseMapper.getOrderItemDetail(id);
        //订单详情
        var order = this.orderService.getDetails(item.getOrderid());
        var orderItemDto = this.beanMap(item, this.currentDTOClass());

        //待审核的+已审核通过的总退款金额
        var sumRefundPrice = order.getRefundItems().stream().filter(e -> e.getStatus() == 1||e.getStatus()==0).map(e -> e.getRefundprice()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //当前项是最后一个退款项
        if (order.getRefundItems().size() == order.getOrderItems().size() - 1) {
            //退款金额就是总支付金额-已退款金额
            orderItemDto.setMaxrefundprice(order.getPayprice().subtract(order.getExpressprice()).subtract(sumRefundPrice));
        } else {

            //订单项金额占订单总额的比例
            var orderItemPriceRatio = item.getSumprice().divide(order.getOrderprice().subtract(order.getExpressprice()), RoundingMode.DOWN).setScale(2);
            var orderItemCheapPrice = BigDecimal.ZERO;

            //积分抵扣
            if (order.getPayscore().compareTo(BigDecimal.ZERO) == 1) {
                orderItemCheapPrice = order.getPayscore().multiply(orderItemPriceRatio);

                //券
            } else if (StringUtils.isNotEmpty(order.getCouponid())) {
                //满减券
                var couponInfo = this.userCouponService.getDetailsById(order.getCouponid());
                if (couponInfo.getCouponInfo().getType() == CouponEnums.CouponTypeEnum.Limit.getValue()) {


                    //是针对某个商品的满减,把满减金额全算到该商品上
                    if (couponInfo.getCouponInfo().getShopscope().equalsIgnoreCase(item.getProductid())) {
                        orderItemCheapPrice = order.getCheap();
                        //全场通用的满减券，按每项的支付金额比例算减免
                    } else if (couponInfo.getCouponInfo().getShopscope().equalsIgnoreCase("all")) {
                        //当前订单项的减免金额
                        orderItemCheapPrice = order.getCheap().multiply(orderItemPriceRatio);
                    }

                    //免费券
                } else if (couponInfo.getCouponInfo().getType() == CouponEnums.CouponTypeEnum.Free.getValue()) {
                    orderItemCheapPrice = item.getProductprice();
                } else {
                    //标签券
                }

            }

            orderItemDto.setMaxrefundprice(item.getSumprice().subtract(orderItemCheapPrice).setScale(2,RoundingMode.DOWN));
        }
        return MyResult.ok(orderItemDto);
    }
}
