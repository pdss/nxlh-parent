package com.nxlh.minapp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.model.MessageModel;
import com.nxlh.manager.service.WebOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@ApiController("/api/wx/weborder")
public class WebOrderController extends BaseController {


    @Reference
    private WebOrderService webOrderService;

    @PostMapping("sync")
    public MessageModel sync(@RequestBody Map<String, Object> model) {
        return this.webOrderService.sync(model);
    }


    /**
     * 同步积分订单
     *
     * @return
     **/
    @PostMapping("syncscore")
    public MessageModel syncsore(@RequestBody Map<String, Object> model) {
        return this.webOrderService.syncScore(model);
    }


    @PostMapping("close")
    public MessageModel close(@RequestParam String ono) {
        return this.webOrderService.close(ono);

    }


}
