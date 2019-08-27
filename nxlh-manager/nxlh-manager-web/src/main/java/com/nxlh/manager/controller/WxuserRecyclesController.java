package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import com.nxlh.manager.model.vo.wxuserRecycles.WxuserRecyclesVO;
import com.nxlh.manager.service.WxUserRecycleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/recycles")
public class WxuserRecyclesController extends BaseController {

    @Reference
    private WxUserRecycleService wxUserRecycleService;


    @GetMapping("listbypage")
    public MyResult listByPage(WxuserRecyclesVO queryVO) throws Exception {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        if (StringUtils.isNotEmpty(queryVO.getFilter())) {
            map.put("filter", queryVO.getFilter());
        }
        var result = this.wxUserRecycleService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }




    @PostMapping("checkprice")
    public MyResult checkPrice(@RequestBody WxUserRecycleDTO wxUserRecycleDTO) {
        var result = this.wxUserRecycleService.checkPrice(wxUserRecycleDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.wxUserRecycleService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.wxUserRecycleService.getById(id);
        return this.ok(obj);
    }

    @GetMapping("cancel")
    public MyResult cancelOrder(@RequestParam String id) {
        var obj = this.wxUserRecycleService.cancel(id);
        return this.ok(obj);
    }

    @GetMapping("close")
    public MyResult closeOrder(@RequestParam String id) {
        var obj = this.wxUserRecycleService.close(id);
        return this.ok(obj);
    }
}
