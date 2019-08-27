package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.SeckillDTO;
import com.nxlh.manager.model.dto.VipShopDTO;
import com.nxlh.manager.model.vo.seckill.SeckillVO;
import com.nxlh.manager.service.SeckillService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/seckill")
public class SeckillController extends BaseController {

    @Reference()
    private SeckillService seckillService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<SeckillVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.seckillService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody SeckillDTO seckillDTO) {
        var result = this.seckillService.addOrUpdate(seckillDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.seckillService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.seckillService.getById(id);
        return this.ok(obj);
    }

    @GetMapping("getall")
    public MyResult getAll() {
        var obj = this.seckillService.getAll();
        return this.ok(obj);
    }


    @PostMapping("editstatus")
    public MyResult editStatus(@RequestBody SeckillDTO seckillDTO) {
        var result = this.seckillService.editStatus(seckillDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }
}
