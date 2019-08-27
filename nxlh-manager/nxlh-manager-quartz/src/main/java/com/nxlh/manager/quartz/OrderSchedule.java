package com.nxlh.manager.quartz;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.manager.service.OrderService;
import com.nxlh.manager.service.WxUserRentShopService;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class OrderSchedule {

    @Reference(timeout = 20000, retries = 1)
    @Lazy
    private OrderService orderService;

    @Reference()
    private WxUserRentShopService wxUserRentShopService;

    /**
     * 每天凌晨1点刷新订单状态
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void checkOrderStatus() {

        //自动关闭订单
        this.orderService.autoCloseOrder();
        //自动确认收货
        this.orderService.autoConfirmReceiveOrder();

    }


    /**
     * 检查租赁订单的状态
     */
//    @Scheduled(fixedRate = 600 * 1000)
//    public void checkRentRecord() {
//
//        try {
//            this.log.info("开始检测租赁订单");
//            this.wxUserRentShopService.autoUpdateRentRecord();
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }


    /**
     * 每天中午12点
     * 检测即将到期的租赁订单并发送短信
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void remindLeaseExpire() {

        try {
            this.log.info("开始检测即将到期的租赁订单并发送短信");
            this.orderService.remindLeaseExpire();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
