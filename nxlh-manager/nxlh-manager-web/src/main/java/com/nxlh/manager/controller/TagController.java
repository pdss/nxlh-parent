package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.TagDTO;
import com.nxlh.manager.model.vo.tag.TagVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.TagService;

import java.util.List;
import java.util.Optional;

@ApiController("/api/web/tag")
public class TagController extends BaseController {

    @Reference
    private TagService tagService;

    @GetMapping("listbypage")
    public MyResult taglist(Optional<TagVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.tagService.page(page, null, ObjectUtils.toArray("istop"), 0);
        return ok(result);
    }

    @PostMapping("/update")
    public MyResult update(@RequestBody TagDTO categoryDTO) {
        try {
            boolean isOk = tagService.addOrUpdate(categoryDTO);
            return MyResult.build(isOk ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
        } catch (Exception e) {
            return json(HttpResponseEnums.BadRequest, false);
        }
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.tagService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.tagService.getById(id);
        return this.ok(obj);
    }

    @PostMapping("getbyname")
    public MyResult getByName(@RequestBody String name) {
        List<TagDTO> list = this.tagService.getByName(name);
        return this.ok(list);
    }

}
