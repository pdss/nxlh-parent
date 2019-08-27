package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.model.vo.coupons.CouponsVO;
import com.nxlh.manager.service.CouponShoptagsService;
import com.nxlh.manager.service.UserCouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.CouponsService;
import com.nxlh.manager.service.ShopService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/coupons")
public class CouponsController extends BaseController {

    @Reference
    private CouponsService couponsService;

    @Reference
    private ShopService shopService;

    @Reference
    private CouponShoptagsService couponShoptagsService;

    @Reference
    private UserCouponService userCouponService;

    @GetMapping("listbypage")
    public MyResult categorylist(Optional<CouponsVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.couponsService.page(page, null, defaultOrderBy, 0);
        return ok(result);
    }

    @PostMapping("/update")
    public MyResult update(@RequestBody CouponsDTO couponsDTO) {
        try {
            boolean isOk = couponsService.addCoupons(couponsDTO);
            return isOk ? ok(true) : json(HttpResponseEnums.InternalServerError, false);
        } catch (Exception e) {
            return json(HttpResponseEnums.BadRequest, false);
        }
    }


    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.couponsService.delCouponByid(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


    @GetMapping("getById")
    public MyResult getById(@RequestParam String id) {
        if (id != null && "".equals(id)) {
            try {
                CouponsDTO byId = couponsService.getById(id);
                return byId != null ? ok(byId) : json(HttpResponseEnums.InternalServerError, null);
            } catch (Exception e) {
                return json(HttpResponseEnums.BadRequest, null);
            }
        }
        return json(HttpResponseEnums.BadRequest, null);
    }

    @PostMapping("getbyname")
    public MyResult getByName(@RequestBody String name) {
        List<CouponsDTO> list = this.couponsService.getByName(name);
        return this.ok(list);
    }

    @PostMapping("sendcoupon")
    public MyResult sendCoupon(@RequestBody CouponsDTO couponsDTO) {
        var result = this.userCouponService.insertCouponByVip(couponsDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

}
