package com.nxlh.manager.quartz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.manager.service.OrderService;
import com.nxlh.manager.service.UserCouponService;
import com.nxlh.manager.service.WxUserRentShopService;
import com.nxlh.manager.service.WxUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;


@SpringBootTest
@RunWith(SpringRunner.class)
public class TestApplication {

    @Reference(timeout = 200000, retries = 1)
    private OrderService orderService;

    @Reference(timeout = 20000, retries = 1)
    private WxUserService wxUserService;

    @Reference
    private WxUserRentShopService wxUserRentShopService;

    @Reference
    private UserCouponService userCouponService;

    @Test
    public void test() throws Exception {
        System.out.println("---------会员降级---------");
        this.wxUserService.checkVipIsInvalidation();
//        三个月无消费记录自动降级
//        this.wxUserService.monthCheckVipIsInvalidation();
        System.out.println("---------会员降级---------");
//        this.wxUserService.checkVipIsInvalidation();

        //自动关闭订单
//        this.orderService.autoCloseOrder();
//        //自动确认收货
//        this.orderService.autoConfirmReceiveOrder();


//        自动关闭订单
//        System.out.println("---------自动关闭订单开始---------");
//        this.orderService.remindLeaseExpire();
//        System.out.println("---------自动关闭订单结束---------");
        //自动确认收货
//        System.out.println("---------自动确认收货开始---------");
//        this.orderService.autoConfirmReceiveOrder();
//        System.out.println("---------自动确认收货结束---------");
//
//        /**
//         * 检查租赁订单的状态
//         */
//        System.out.println("---------检查租赁订单的状态开始---------");
//        this.wxUserRentShopService.autoUpdateRentRecord();
//        System.out.println("---------检查租赁订单的状态结束---------");
//
//
//        //发送优惠券
//
//        //判断优惠券是否过期
//        System.out.println("---------判断优惠券是否过期开始---------");
//        this.userCouponService.checkOverdueCoupon();
//        System.out.println("---------判断优惠券是否过期结束---------");
//        //每月1号凌晨2点发放优惠券
//        System.out.println("---------每月1号凌晨2点发放优惠券开始---------");
//        this.userCouponService.sendFixedCoupon();
//        System.out.println("---------每月1号凌晨2点发放优惠券结束---------");
//        System.out.println("---------会员降级开始---------");
////        this.wxUserService.checkVipIsInvalidation();
//        System.out.println("---------会员降级结束---------");

    }
}
