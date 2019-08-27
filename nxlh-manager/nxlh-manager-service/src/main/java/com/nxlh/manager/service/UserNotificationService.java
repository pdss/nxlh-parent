package com.nxlh.manager.service;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbUserNotificationMapper;
import com.nxlh.manager.model.dbo.TbUserNotification;
import com.nxlh.manager.model.dto.UserNotificationDTO;

import java.util.List;

public interface UserNotificationService extends BaseService<UserNotificationDTO, TbUserNotificationMapper, TbUserNotification> {

    /**
     * 删除消息管理记录的用户关联表
     *
     * @param id
     * @return
     */
    boolean removeByUotificationId(String id);

    /**
     * 发送信息 根据消息id关联所有用户id批量插入关联表中
     *
     * @param nid
     * @return
     */
    boolean sendMsgByNotification(String nid);


    /**
     * 发货通知
     *
     * @param wxuserid
     * @param shopname
     * @param Orderid
     * @return
     */
    boolean sendMsgByTransit(String wxuserid, String shopname, String Orderid);


    /**
     * 获取用户的消息通知
     *
     * @param  page
     * @param wxUserId
     * @return
     */
    Pagination<UserNotificationDTO> getUserNotis(PageParameter page, String wxUserId);

}
