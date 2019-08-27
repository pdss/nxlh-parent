package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.service.StatisticsService;
import org.springframework.web.bind.annotation.GetMapping;

@ApiController("/api/web/index")
public class IndexController extends BaseController {

    @Reference
    private StatisticsService statisticsService;


    @GetMapping("summary")
    public MyResult summary() {
        return this.statisticsService.summary();
    }


}
