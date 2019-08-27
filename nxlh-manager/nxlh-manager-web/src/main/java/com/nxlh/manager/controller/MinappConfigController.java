package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.MinappConfigDTO;
import com.nxlh.manager.model.vo.minappConfig.MinappConfigVO;
import com.nxlh.manager.service.MinappConfigService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/minappconfig")
public class MinappConfigController extends BaseController {

    @Reference()
    private MinappConfigService minappConfigService;

    @PostMapping("update")
    public MyResult update(@RequestBody MinappConfigDTO minappConfigDTO) {
        var result = this.minappConfigService.updateById(minappConfigDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }
    
    @GetMapping("getbgimg")
    public MyResult getBgImg() {
        var result = this.minappConfigService.getbgImg();
        return this.ok(result);
    }


}
