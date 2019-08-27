package com.nxlh.manager.service;


import com.nxlh.common.model.MyResult;

public interface StatisticsService extends BaseRedisCURDService {

    /**
     * 今日数据汇总
     *
     * @return
     */
    MyResult summary();


}
