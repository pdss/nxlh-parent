package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.service.StatisticsService;

import java.util.HashMap;

import static com.nxlh.manager.rediskey.Keys.*;


@Service(interfaceClass = StatisticsService.class)
public class StatisticsServiceImpl extends BaseRedisCURDServiceImpl implements StatisticsService {


    @Override
    public MyResult summary() {

//        var today_orderCount = (int) this.redisService.get(SUMMARY_TODAY_ORDER_COUNT_KEY);
//        var today_wxuserCount = (int) this.redisService.get(SUMMARY_TODAY_NEWWXUSER_COUNT_KEY);
//        var today_request = (int) this.redisService.get(SUMMARY_TODAY_WXUSER_REQUEST_COUNT_KEY);
//        var today_updateShop = (int) this.redisService.get(SUMMARY_TODAY_UPDATE_SHOP_COUNT_KEY);
//        var today_refundOrder = (int) this.redisService.get(SUMMARY_TODAY_ORDERREFUND_COUNT_KEY);
//
//        var month_order = (int) this.redisService.get(SUMMARY_MONTH_ORDER_COUNT_KEY);
//        var month_wxuser = (int) this.redisService.get(SUMMARY_MONTH_NEWWXUSER_COUNT_KEY);
//        var month_request = (int) this.redisService.get(SUMMARY_MONTH_WXUSER_REQUEST_COUNT_KEY);
//        var month_updateShop = (int) this.redisService.get(SUMMARY_MONTH_UPDATE_SHOP_COUNT_KEY);
//        var month_refundOrder = (int) this.redisService.get(SUMMARY_MONTH_ORDERREFUND_COUNT_KEY);
//
//
//        var map = new HashMap<String, Integer>() {{
//            put("today_order_count", today_orderCount);
//            put("today_wxuser_count", today_wxuserCount);
//            put("today_request", today_request);
//            put("today_updateshop", today_updateShop);
//            put("today_refundorder", today_refundOrder);
//            put("month_order", month_order);
//            put("month_wxuser", month_wxuser);
//            put("month_request", month_request);
//            put("month_updateshop", month_updateShop);
//            put("month_refundorder", month_refundOrder);
//        }};
//
//        return MyResult.ok(map);
        return null;

    }
}
