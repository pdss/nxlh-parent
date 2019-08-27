package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbUserNotificationMapper;
import com.nxlh.manager.model.dbo.TbUserNotification;
import com.nxlh.manager.model.dto.UserNotificationDTO;
import com.nxlh.manager.model.enums.NotificationEnums;
import com.nxlh.manager.service.UserNotificationService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

@Service(interfaceClass = UserNotificationService.class)
public class UserNotificationServiceImpl extends BaseDbCURDSServiceImpl<TbUserNotificationMapper, TbUserNotification, UserNotificationDTO> implements UserNotificationService {

    @Override
    public boolean removeByUotificationId(String id) {
        Example example = Example.builder(TbUserNotification.class).where(Sqls.custom().andEqualTo("nid", id)).build();
        this.baseMapper.deleteByExample(example);
        return true;
    }

    /**
     * 发送信息 根据消息id关联所有用户id批量插入关联表中
     *
     * @param nid
     * @return
     */
    @Override
    public boolean sendMsgByNotification(String nid) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.baseMapper.insertByNotificationId(nid);
        });
        return result;
    }

    @Override
    public boolean sendMsgByTransit(String wxuserid, String shopname, String Orderid) {
        return this.baseMapper.insertBySystem(wxuserid, shopname, "", Orderid, NotificationEnums.NotificationTypeEnums.user.getValue());
    }

    @Override
    public Pagination<UserNotificationDTO> getUserNotis(PageParameter page, String wxUserId) {

        var example = this.sqlBuilder().where(Sqls.custom().andEqualTo("userid", wxUserId)).build();
        return this.page(page, example);
    }
}
