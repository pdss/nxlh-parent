package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.NotificationDTO;
import com.nxlh.manager.model.vo.notification.NotificationVO;
import com.nxlh.manager.service.NotificationService;
import com.nxlh.manager.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/notification")
public class NotificationController extends BaseController {


    @Reference
    private NotificationService notificationService;

    @Reference
    private UserNotificationService userNotificationService;

    @GetMapping("listbypage")
    public MyResult listByPage(Optional<NotificationVO> queryVO) {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("isdelete", 0);
        var result = this.notificationService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }

    @PostMapping("update")
    public MyResult update(@RequestBody NotificationDTO notificationDTO) {
        var result = this.notificationService.addOrUpdate(notificationDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.notificationService.removeNotificationByid(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.notificationService.getById(id);
        return this.ok(obj);
    }

    @GetMapping("sendmsg")
    public MyResult sendMsg(@RequestParam String id) {
        var obj = this.userNotificationService.sendMsgByNotification(id);
        return this.ok(obj);
    }
}
