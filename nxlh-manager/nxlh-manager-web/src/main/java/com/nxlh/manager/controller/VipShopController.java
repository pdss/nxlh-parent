package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.VipShopDTO;
import com.nxlh.manager.model.vo.vipShop.VipShopVO;
import com.nxlh.manager.service.VipShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/vipshop")
public class VipShopController extends BaseController {

    @Reference()
    private VipShopService vipShopService;


    @GetMapping("listbypage")
    public MyResult listByPage(VipShopVO queryVO) {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("activityid", queryVO.getFilter());
        var result = this.vipShopService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }

    @PostMapping("update")
    public MyResult update(@RequestBody VipShopDTO vipShopDTO) {
        var result = this.vipShopService.addOrUpdateVipShop(vipShopDTO);
        return result;
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.vipShopService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.vipShopService.getById(id);
        return this.ok(obj);
    }

    @PostMapping("editstatus")
    public MyResult editStatus(@RequestBody VipShopDTO vipShopDTO) {
        var result = this.vipShopService.editStatus(vipShopDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

}
