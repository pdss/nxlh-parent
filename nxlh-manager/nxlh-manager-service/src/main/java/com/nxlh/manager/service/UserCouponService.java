package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbUserCouponMapper;
import com.nxlh.manager.model.dbo.TbUserCoupon;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.model.dto.UserCouponDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.enums.CouponEnums;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserCouponService extends BaseService<UserCouponDTO, TbUserCouponMapper, TbUserCoupon> {

    /**
     * 检查用户优惠券过期状态
     *
     * @return
     */
    boolean checkOverdueCoupon();

    /**
     * 获取用户的优惠券
     *
     * @param userid
     * @return
     */
    List<UserCouponDTO> getCouponsByUserId(String userid);

    /**
     * 获取指定状态的用户的优惠券
     *
     * @param page
     * @param userid
     * @param status 券的状态
     * @return
     */
    Pagination<UserCouponDTO> getCouponsByStatus(PageParameter page, String userid, CouponEnums.CouponStatusEnum status);


    /**
     * 获取用户优惠券的详情
     *
     * @param user_coupon_id
     * @return
     */
    UserCouponDTO getDetailsById(String user_coupon_id);

    /**
     * 根据订单信息，获取可使用的优惠券
     *
     * @param orderno
     * @return
     */
    MyResult getValidCouponsByOrder(String userid, String orderno);


    /**
     * 插入用户关联优惠卷的数据
     *
     * @return
     */
    boolean insertWxuserCoupons(WxUserDTO wxUserDTO);

    /***
     * 获取可领取的券
     * @return
     */
    MyResult getCanReceiveCoupons(String wxUserId);


    /**
     * 领券
     *
     * @param couponId
     * @return
     */
    MyResult receive(String wxUserId, String couponId);

    /**
     * 根据会员插入优惠券
     *
     * @return
     */
    boolean insertCouponByVip(CouponsDTO couponsDTO);

    /**
     * 指定的商品id中过滤 能使用该券的商品信息
     *
     * @param couponId
     * @return
     */
    List<ShopDTO> getTagProductInfoByCouponId(@Param("couponid") String couponId, @Param("productids") List<String> productIds);


    /**
     * 发放每月固定优惠卷
     *
     * @return
     */
    boolean sendFixedCoupon();


    /**
     * 检查券的有效性
     *
     * @param userCouponDTOS
     * @return
     */
    List<UserCouponDTO> checkCouponValid(List<UserCouponDTO> userCouponDTOS);

}
