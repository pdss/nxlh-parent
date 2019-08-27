package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.AdminDTO;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.model.vo.admin.AdminVO;
import com.nxlh.manager.model.vo.admin.LoginVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.nxlh.manager.service.AdminService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApiController("/api/web/admin")
public class AdminController extends BaseController {

    @Reference
    private AdminService adminService;


    @PostMapping("login")
    public MyResult login(@RequestBody LoginVO loginVO) throws Exception {
        return this.adminService.login(loginVO.getLoginname(), loginVO.getLoginpassword());


    }

    @GetMapping("listbypage")
    public MyResult listByPage(Optional<AdminVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.adminService.page(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @PostMapping("update")
    public MyResult update(@RequestBody AdminDTO adminDTO) {
        var result = this.adminService.addOrUpdateAdmin(adminDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.adminService.removeById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.adminService.getById(id);
        return this.ok(obj);


    }

    @GetMapping("menubyrole")
    public MyResult menubyrole() throws UnAuthorizeException {

        String roleid = this.getUserInfo().getRoleid();

        return this.adminService.getMenuByRoleId(roleid);

    }

    @GetMapping("unauth")
    public MyResult unauth() {
        return MyResult.build(HttpResponseEnums.UnAuthorized, "");
    }


}
