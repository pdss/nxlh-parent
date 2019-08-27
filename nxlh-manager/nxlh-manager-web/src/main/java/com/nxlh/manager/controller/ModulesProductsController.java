package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.ModulesProductsDTO;
import com.nxlh.manager.model.vo.modulesProducts.ModulesProductsVO;
import com.nxlh.manager.service.ModulesProductsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/modulesproducts")
public class ModulesProductsController extends BaseController {

    @Reference()
    private ModulesProductsService modulesProductsService;


    @GetMapping("listbypage")
    public MyResult listByPage(ModulesProductsVO queryVO) {
        var page = this.makePage(queryVO);

        var result = this.modulesProductsService.page(page, queryVO.getFilter());
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody ModulesProductsDTO modulesProductsDTO) {
        boolean b = this.modulesProductsService.checkProduct(modulesProductsDTO);
        //是否重复添加
        if (b) {
            var result = this.modulesProductsService.addOrUpdate(modulesProductsDTO);
            return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, true);
        }
        return MyResult.build(HttpResponseEnums.Ok.getValue(), "商品已存在", false);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.modulesProductsService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.modulesProductsService.getById(id);
        return this.ok(obj);
    }


}
