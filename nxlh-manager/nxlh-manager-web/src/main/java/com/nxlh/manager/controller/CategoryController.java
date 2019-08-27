package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.CategoryDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.category.CategoryVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.CategoryService;

import java.util.List;
import java.util.Optional;

@ApiController("/api/web/category")
public class CategoryController extends BaseController {

    @Reference
    private CategoryService categoryService;

    @GetMapping("listbypage")
    public MyResult categorylist(Optional<CategoryVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.categoryService.page(page, null, ObjectUtils.toArray("sort"), 1);
        return ok(result);
    }


    @PostMapping("/update")
    public MyResult update(@RequestBody CategoryDTO categoryDTO) {
        try {
            boolean isOk = categoryService.addOrUpdate(categoryDTO);
            return MyResult.build(isOk ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
        } catch (Exception e) {
            return json(HttpResponseEnums.BadRequest, false);
        }
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.categoryService.delByid(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.categoryService.getById(id);
        return this.ok(obj);
    }

    @PostMapping("getbyname")
    public MyResult getByName(@RequestBody String name) {
        List<CategoryDTO> list = this.categoryService.getByName(name);
        return this.ok(list);
    }

    @GetMapping("getall")
    public MyResult getAll() {
        var obj = this.categoryService.getAll();
        return this.ok(obj);
    }

    @GetMapping("ergodic")
    public MyResult ergodic() {
        var obj = this.categoryService.ergodic();
        return this.ok(obj);
    }

    @GetMapping("getallbyshop")
    public MyResult getAllByShop(@RequestParam String id) {
        var list = this.categoryService.queryAllCategoryByShopId(id);
        return this.ok(list);
    }


}
