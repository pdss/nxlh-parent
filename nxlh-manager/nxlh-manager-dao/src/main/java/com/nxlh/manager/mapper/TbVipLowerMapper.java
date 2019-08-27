package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbVipLower;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface TbVipLowerMapper extends Mapper<TbVipLower> {

    /**
     * 根据日期获取三个月内降过级的用户
     *
     * @param date
     * @return
     */
    List<TbVipLower> getByUserIds(@Param("date") Date date);
//    List<TbVipLower> getByUserIds(@Param("userids") List<String> userids, @Param("date") Date date);
}