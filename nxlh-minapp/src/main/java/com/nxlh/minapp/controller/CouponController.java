package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.manager.model.enums.CouponEnums;
import com.nxlh.manager.service.UserCouponService;
import com.nxlh.minapp.model.CouponStatusVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController("/api/wx/coupon")
public class CouponController extends BaseController {

    @Reference
    private UserCouponService userCouponService;


    /**
     * 获取用户指定状态的优惠券(分页)
     * 优惠券页面
     * @return
     */
    @PostMapping("getusercoupons")
    public MyResult getUserCoupons(@RequestBody CouponStatusVO req) throws UnAuthorizeException {
        var statusEnum = CouponEnums.CouponStatusEnum.getValue(req.getStatus());
        var list = this.userCouponService.getCouponsByStatus(new PageParameter(req.getPageIndex()), this.getUserId(), statusEnum);
        return MyResult.ok(list);
    }


    /**
     * 根据订单信息，获取可使用的优惠券
     * @return
     */
    @GetMapping("getvalidcoupons")
    public MyResult getValidCouponsByOrder(@RequestParam String orderno) throws UnAuthorizeException {
        return this.userCouponService.getValidCouponsByOrder(this.getUserId(),orderno);
    }


    /**
     * 获取可领取的券
     * @return
     */
    @GetMapping("receivecoupon")
    public MyResult getReceiveCoupon() throws UnAuthorizeException {
        return this.userCouponService.getCanReceiveCoupons(this.getUserId());
    }


    /**
     * 领券
     * @param id  优惠券id
     * @return
     */
    @GetMapping("receive")
    public MyResult receive(@RequestParam String id) throws UnAuthorizeException {
         return this.userCouponService.receive(this.getUserId(),id);
    }



}
