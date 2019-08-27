package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.IntervalSendcouponDTO;
import com.nxlh.manager.model.vo.intervalSendcoupon.IntervalSendcouponVO;
import com.nxlh.manager.service.IntervalSendcouponService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/intervalsendcoupon")
public class IntervalSendcouponController extends BaseController {

    @Reference()
    private IntervalSendcouponService intervalSendcouponService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<IntervalSendcouponVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.intervalSendcouponService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody IntervalSendcouponDTO bannerDTO) {
        var result = this.intervalSendcouponService.addOrUpdate(bannerDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.intervalSendcouponService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.intervalSendcouponService.getById(id);
        return this.ok(obj);
    }


}
