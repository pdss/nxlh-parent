package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.model.dto.WxuserLotteryDTO;
import com.nxlh.manager.model.vo.wxuserLottery.WxuserLotteryVO;
import com.nxlh.manager.service.LotteryService;
import com.nxlh.manager.service.WxuserLotteryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@ApiController("/api/web/wxuserlottery")
public class WxuserLotteryController extends BaseController {

    @Reference()
    private WxuserLotteryService wxuserLotteryService;

    @Reference()
    private LotteryService lotteryService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<WxuserLotteryVO> queryVO) {
        var page = this.makePage(queryVO);
        var result = this.lotteryService.wxUserLotterypage(page, null, defaultOrderBy, 1);
        return ok(result);
    }


    @GetMapping("infolistbypage")
    public MyResult infoListByPage(@RequestParam String id) {
        var result = this.wxuserLotteryService.queryAllByLotteryId(id);
        return ok(result);
    }


    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.wxuserLotteryService.deleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


    @GetMapping("exchange")
    public MyResult exchange(@RequestParam String id) {
        var result = this.wxuserLotteryService.exchange(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }


}
