package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.BannerDTO;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.model.vo.banner.BannerVO;
import com.nxlh.manager.model.vo.shop.ShopQueryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.BannerService;

import java.util.Optional;

@ApiController("/api/web/banner")
public class BannerController extends BaseController {

    @Reference()
    private BannerService bannerService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<BannerVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.bannerService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody BannerDTO bannerDTO) {
        var result = this.bannerService.addOrUpdate(bannerDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.bannerService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.bannerService.getById(id);
        return this.ok(obj);
    }

    @PostMapping("editstatus")
    public MyResult editstatus(@RequestBody BannerDTO bannerDTO) {
        var result = this.bannerService.editStatus(bannerDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

}
