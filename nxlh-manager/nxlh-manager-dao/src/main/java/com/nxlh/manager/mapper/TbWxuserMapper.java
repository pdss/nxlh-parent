package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dto.WxUserDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TbWxuserMapper extends Mapper<TbWxuser> {

    /**
     * 3个月内没有消费记录的微信用户id,限铜牌和银牌
     *
     * @param start
     * @return
     */
    List<WxUserDTO> getNoOrderRecordsIn3Months(@Param("ids") List<String> ids, @Param("starttime") Date start);


    /**
     * 批量降级会员等级
     *
     * @return
     */
    boolean degradeVip(List<String> wxUserIds);

    /**
     * 根据vipid查询用户id
     *
     * @param vipid
     * @return
     */
    List<String> getWxuserByVip(@Param("vipid") Integer vipid);

    /**
     * 查询所有非会员和最高等级会员的微信用户
     *
     * @param vipid
     * @return
     */
    List<TbWxuser> getWxuserByVipList(@Param("vipid") Integer vipid);
}