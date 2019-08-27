package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.IndexcategoryDTO;
import com.nxlh.manager.model.vo.indexCategory.IndexcategoryVO;
import com.nxlh.manager.service.IndexcategoryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@ApiController("/api/web/indexcategory")
public class IndexcategoryController extends BaseController {

    @Reference
    private IndexcategoryService indexcategoryService;

    @GetMapping("listbypage")
    public MyResult categorylist(Optional<IndexcategoryVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.indexcategoryService.page(page, null, ObjectUtils.toArray("sort"), 1);
        return ok(result);
    }


    @PostMapping("/update")
    public MyResult update(@RequestBody IndexcategoryDTO indexcategoryDTO) {
        try {
            boolean isOk = indexcategoryService.addOrUpdate(indexcategoryDTO);
            return MyResult.build(isOk ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
        } catch (Exception e) {
            return json(HttpResponseEnums.BadRequest, false);
        }
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.indexcategoryService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


}
