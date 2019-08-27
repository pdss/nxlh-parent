package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbUserNotification;
import io.lettuce.core.dynamic.annotation.Param;
import tk.mybatis.mapper.common.Mapper;

public interface TbUserNotificationMapper extends Mapper<TbUserNotification> {

    /**
     * 根据消息管理记录id关联所用微信用户id批量插入关联表
     *
     * @param nid 管理记录id
     * @return
     */
    boolean insertByNotificationId(@Param("nid") String nid);

    /**
     * 系统发送消息给个人
     *
     * @param userid
     * @param title
     * @param subtitle
     * @param extra
     * @return
     */
    boolean insertBySystem(@Param("userid") String userid, @Param("title") String title, @Param("subtitle") String subtitle, @Param("extra") String extra, @Param("ntype2") Integer ntype2);
}