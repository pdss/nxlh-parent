package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.menu.MenuVO;
import com.nxlh.manager.service.MenuService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/menu")
public class MenuController extends BaseController {

    @Reference
    private MenuService menuService;

    @GetMapping("listbypage")
    public MyResult listByPage(Optional<MenuVO> queryVO) {
        var page = this.makePage(queryVO);
        Map<String, Object> map = new HashMap<>();
        map.put("isdelete", 0);
        var result = this.menuService.pageMenu(page, map, ObjectUtils.toArray("sort"), 1);
        return ok(result);
    }

    @PostMapping("update")
    public MyResult update(@RequestBody MenuDTO menuDTO) {
        var result = this.menuService.addOrUpdate(menuDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {

        var result = this.menuService.removeMenuById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.menuService.getById(id);
        return this.ok(obj);
    }

    @GetMapping("getall")
    public MyResult getAll() {
        List<MenuDTO> all = this.menuService.getALL();
        return this.ok(all);
    }


}
