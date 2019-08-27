package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.service.CategoryService;
import org.springframework.web.bind.annotation.GetMapping;


@ApiController("/api/wx/category")
public class CategoryController extends BaseController {

    @Reference
    private CategoryService categoryService;


    @GetMapping("getall")
    public MyResult getAll() {
        String[] sort = {"sort"};
//        var list = this.categoryService.list(null, sort, 1);
        var list = this.categoryService.ergodic();
        return ok(list);
    }




}
