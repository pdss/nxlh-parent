package com.nxlh.manager.quartz;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.manager.service.WxUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WxUserSchedule {

    @Reference(timeout = 20000, retries = 1)
    private WxUserService wxUserService;

    /**
     * 每天凌晨2点刷新会员经验值
     * 银牌、铜牌，金牌都降级 将经验降为0 自动降级 经验值达到下一个等级 升级
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void checkOrderStatus() throws Exception {
        this.wxUserService.checkVipIsInvalidation();
//        三个月无消费记录自动降级
        this.wxUserService.monthCheckVipIsInvalidation();
    }

    @Scheduled(cron = "0 0/5 * * * ? *")
    public void Resend() {
        //每5分钟，重新发送发送失败的修改用户等级的请求
        this.wxUserService.ResendEditUserType();
    }


}
