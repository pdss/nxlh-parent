package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.manager.service.WxUserService;
import org.springframework.web.bind.annotation.GetMapping;

@ApiController("/api/wx/test")
public class TestController extends BaseController {

    @Reference(timeout = 20000,retries = 0)
    private WxUserService wxUserService;

    @GetMapping("refreshuser")
    private Object refreshUserRDS(){
        this.wxUserService.refreshUserRedis();
        return "Refresh Success!";
    }
}
