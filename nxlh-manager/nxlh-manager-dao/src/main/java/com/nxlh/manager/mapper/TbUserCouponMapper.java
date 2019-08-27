package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbUserCoupon;
import com.nxlh.manager.model.dto.ShopDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.Date;
import java.util.List;

public interface TbUserCouponMapper extends Mapper<TbUserCoupon> {

    /**
     * 过期无效券
     *
     * @param list
     * @return
     */
    boolean batchOverdue(List<String> list);

    /**
     * 获取用户的优惠券列表(详情)
     *
     * @param userid
     * @return
     */
    List<TbUserCoupon> getCouponsByUserId(String userid);


    /**
     * 获取指定状态的用户的优惠券列表(详情)
     *
     * @param userid
     * @param status
     * @return
     */
    List<TbUserCoupon> getCouponsByStatus(@Param("userid") String userid, @Param("status") int status);


    /**
     * 获取指定的用户优惠券详情
     *
     * @param id 关联id
     * @return
     */
    TbUserCoupon getUserCouponInfoById(String id);


    /**
     * 用户是否已领取过指定券
     *
     * @param couponid
     * @param wxuserid
     * @return
     */
    Integer userHasCouponById(@Param("couponid") String couponid, @Param("wxuserid") String wxuserid);

    /**
     * 根据会员id 批量插入
     *
     * @param userid
     * @param couponid
     * @param overdate
     * @return
     */
    boolean insertByVip(@Param("userid") String userid, @Param("couponid") String couponid, @Param("overdate") Date overdate);


    /**
     * 获取券指定可用的标签的商品id
     *
     * @return
     */
    List<ShopDTO> getTagProductIdByCouponId(String couponId, List<String> productIds);

}