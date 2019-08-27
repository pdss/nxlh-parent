package com.nxlh.manager.controller;

import com.nxlh.common.annotation.ApiController;
import org.springframework.web.bind.annotation.GetMapping;

@ApiController("/api")
public class TestController extends BaseController {

    @GetMapping("/")
    public Object test(){
          return "Test Ok";
    }
}
