package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbWxuserRentshop;
import com.nxlh.manager.model.dto.WxUserRentShopDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface TbWxuserRentshopMapper extends Mapper<TbWxuserRentshop> {


    /**
     * 用户是否有正在租赁的商品
     *
     * @param wxuserid
     * @return
     */
    TbWxuserRentshop isExistValidRentByWxUserId(String wxuserid);


    /**
     * 根据订单id查找出租记录
     *
     * @param orderid
     * @return
     */
    TbWxuserRentshop getByOrderId(String orderid);

    /**
     * 指定时间段的租赁记录数
     * @param startDate
     * @return
     */
    Long getRentRecordInDateTime(@Param("date") Date startDate, @Param("wxuserid") String wxuserd ,@Param("protype") int protype);


    /**
     * 根据日期获取逾期用户记录
     * @param date
     * @return
     */
    List<WxUserRentShopDTO> getRentOverdue(@Param("date") Date date);


    List<WxUserRentShopDTO> getEndDateIsNullOrder();
}