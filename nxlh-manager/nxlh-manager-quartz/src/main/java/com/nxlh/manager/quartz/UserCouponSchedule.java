package com.nxlh.manager.quartz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.manager.service.UserCouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UserCouponSchedule {


    @Reference
    private UserCouponService userCouponService;


    //判断优惠券是否过期
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkOverdueCoupon() {
        this.userCouponService.checkOverdueCoupon();
    }


    /**
     * 每月1号凌晨2点发放优惠券
     */
    @Scheduled(cron = "0 0 2 1 * ?")
    public void sendFixedCoupon() {
        this.userCouponService.sendFixedCoupon();
    }

}
