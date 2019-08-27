package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.model.dto.RoleDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.role.RoleVO;
import com.nxlh.manager.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/role")
public class RoleController extends BaseController {

    @Reference
    private RoleService roleService;

    @GetMapping("listbypage")
    public MyResult listByPage(Optional<RoleVO> queryVO) {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("isdelete", 0);
        var result = this.roleService.page(page, map, defaultOrderBy, 1);
        return ok(result);
    }

    @PostMapping("update")
    public MyResult update(@RequestBody RoleDTO roleDTO) {
        var result = this.roleService.addOrUpdateRole(roleDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @PostMapping("updaterolemenu")
    public MyResult updateRoleMenu(@RequestBody RoleDTO roleDTO) {
        var result = this.roleService.updateRoleMenu(roleDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.roleService.removeRoleByid(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.roleService.getById(id);
        return this.ok(obj);
    }

    @PostMapping("getbyname")
    public MyResult getByName(@RequestBody String name) {
        List<RoleDTO> list = this.roleService.getByName(name);
        return this.ok(list);
    }

    @GetMapping("getallroles")
    public MyResult getAllRoles() {
        List<RoleDTO> list = this.roleService.getALL();
        return this.ok(list);
    }

    @GetMapping("getallauthorize")
    public MyResult getAllRoles(@RequestParam String id) {
        List<AuthorizeDTO> list = this.roleService.getAuthorizeByRoleid(id);
        return this.ok(list);
    }

    @PostMapping("updateroleAuthorize")
    public MyResult updateRoleMenu(@RequestBody List<AuthorizeDTO> authorizeDTOs) {
        var result = this.roleService.insertRoleAuthroizr(authorizeDTOs);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

}
