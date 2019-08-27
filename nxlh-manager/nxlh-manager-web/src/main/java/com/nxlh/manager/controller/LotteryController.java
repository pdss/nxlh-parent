package com.nxlh.manager.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.LotteryDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.lottery.LotteryVO;
import com.nxlh.manager.model.vo.lottery.LotteryVO;
import com.nxlh.manager.service.LotteryService;
import com.nxlh.manager.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@ApiController("/api/web/lottery")
public class LotteryController extends BaseController {

    @Reference
    private LotteryService lotteryService;


    @GetMapping("listbypage")
    public MyResult listbypage(Optional<LotteryVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.lotteryService.page(page, null, defaultOrderBy, 0);
        return ok(result);
    }

    @PostMapping("/update")
    public MyResult update(@RequestBody LotteryDTO lotteryDTO) {
        try {
            boolean isOk = lotteryService.addOrUpdateLottery(lotteryDTO);
            return isOk ? ok(true) : json(HttpResponseEnums.InternalServerError, false);
        } catch (Exception e) {
            return json(HttpResponseEnums.BadRequest, false);
        }
    }


    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        //关闭活动
        var result = this.lotteryService.closeActivity(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        if (StringUtils.isNotEmpty(id)) {
            LotteryDTO lottery = lotteryService.getLotteryById(id);
            return ok(lottery);
        }
        return json(HttpResponseEnums.BadRequest, null);
    }

    @PostMapping("editstatus")
    public MyResult editstatus(@RequestBody LotteryDTO lotteryDTO) {
        var result = this.lotteryService.editStatus(lotteryDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @PostMapping("editshow")
    public MyResult editShow(@RequestBody LotteryDTO lotteryDTO) {
        var result = this.lotteryService.editShow(lotteryDTO);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


}
