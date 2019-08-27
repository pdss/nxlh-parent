package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.RecycleProductDTO;
import com.nxlh.manager.model.vo.recycleProduct.RecycleProductVO;
import com.nxlh.manager.service.RecycleProductService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/recycleproduct")
public class RecycleProductController extends BaseController {
    @Reference
    private RecycleProductService recycleProductService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<RecycleProductVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.recycleProductService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody RecycleProductDTO recycleProductDTO) {
        var result = this.recycleProductService.addOrUpdateRecycleProduct(recycleProductDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.recycleProductService.recycleProductDeleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.recycleProductService.getById(id);
        return this.ok(obj);
    }


}
