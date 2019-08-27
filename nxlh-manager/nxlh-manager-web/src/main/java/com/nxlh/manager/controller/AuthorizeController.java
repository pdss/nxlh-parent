package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.model.vo.authorize.AuthorizeVO;
import com.nxlh.manager.service.AuthorizeService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/authorize")
public class AuthorizeController extends BaseController {

    @Reference
    private AuthorizeService authorizeService;

    @GetMapping("listbypage")
    public MyResult listByPage(Optional<AuthorizeVO> queryVO) {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("isdelete", 0);
        var result = this.authorizeService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }

    @PostMapping("update")
    public MyResult update(@RequestBody AuthorizeDTO authorizeDTO) {
        var result = this.authorizeService.addOrUpdate(authorizeDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.authorizeService.removeById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.authorizeService.getById(id);
        return this.ok(obj);
    }


}
