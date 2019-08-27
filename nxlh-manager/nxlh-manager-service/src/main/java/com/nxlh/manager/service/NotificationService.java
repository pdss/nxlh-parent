package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbNotificationMapper;
import com.nxlh.manager.model.dbo.TbNotification;
import com.nxlh.manager.model.dto.NotificationDTO;
import com.nxlh.manager.model.enums.ExpressEnums;

import java.util.List;

public interface NotificationService extends BaseService<NotificationDTO, TbNotificationMapper, TbNotification> {


    /**
     * 删除消息管理记录的用户关联表
     *
     * @param id
     * @return
     */
    boolean removeNotificationByid(String id);



}
