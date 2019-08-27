package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbNotificationMapper;
import com.nxlh.manager.model.dbo.TbNotification;
import com.nxlh.manager.model.dto.NotificationDTO;
import com.nxlh.manager.service.NotificationService;
import com.nxlh.manager.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = NotificationService.class)
public class NotificationServiceImpl extends BaseDbCURDSServiceImpl<TbNotificationMapper, TbNotification, NotificationDTO> implements NotificationService {

    @Autowired
    private UserNotificationService userNotificationService;


    @Override
    public boolean removeNotificationByid(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.deleteById(id);
            this.userNotificationService.removeByUotificationId(id);
        });
        return result;
    }

}

