package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbTagMapper;
import com.nxlh.manager.mapper.TbWxuserRentshopMapper;
import com.nxlh.manager.model.dbo.TbTag;
import com.nxlh.manager.model.dbo.TbWxuserRentshop;
import com.nxlh.manager.model.dto.TagDTO;
import com.nxlh.manager.model.dto.WxUserRentShopDTO;

import java.util.Date;
import java.util.List;

public interface WxUserRentShopService extends BaseService<WxUserRentShopDTO, TbWxuserRentshopMapper, TbWxuserRentshop> {


    /**
     * 用户是否存在正在租赁的商品
     *
     * @param userid
     * @return
     */
    WxUserRentShopDTO isExistValidRentByWxUserId(String userid);


    /**
     * 根据订单id获取出租记录
     *
     * @param orderId
     * @return
     */
    WxUserRentShopDTO getByOrderId(String orderId);


    /**
     * 自动更新租赁记录的状态
     *
     * @return
     */
    int autoUpdateRentRecord();

    /**
     * 根据日期获取逾期订单
     *
     * @param date
     * @return
     */
    List<WxUserRentShopDTO> getOverdue(Date date);

    /**
     * 指定时间段的租赁数
     *
     * @param startTIme
     * @param protype   商品类型 1软件 2硬件 0忽略
     * @return
     */
    Long getRentRecordInDateTime(Date startTIme, String wxUserid, int protype);

    List<WxUserRentShopDTO> getEndDateIsNullOrder();

}
