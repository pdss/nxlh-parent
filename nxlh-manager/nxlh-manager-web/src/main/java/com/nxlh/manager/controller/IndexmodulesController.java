package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.IndexmodulesDTO;
import com.nxlh.manager.model.vo.indexModules.IndexmodulesVO;
import com.nxlh.manager.service.IndexmodulesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/indexmodules")
public class IndexmodulesController extends BaseController {

    @Reference()
    private IndexmodulesService indexModulesService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<IndexmodulesVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.indexModulesService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody IndexmodulesDTO indexModulesDTO) {
        var result = this.indexModulesService.addOrUpdate(indexModulesDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.indexModulesService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.indexModulesService.getById(id);
        return this.ok(obj);
    }


}
