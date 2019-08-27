package com.nxlh.minapp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.LotteryDTO;
import com.nxlh.manager.model.dto.LotteryItemDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.lottery.LotteryVO;
import com.nxlh.manager.model.vo.lottery.LotteryVO;
import com.nxlh.manager.service.LotteryService;
import com.nxlh.manager.service.ShopService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@ApiController("/api/wx/lottery")
public class LotteryController extends BaseController {

    @Reference
    private LotteryService lotteryService;

    /**
     * 抽奖详情
     *
     * @param id
     * @return
     */
    @GetMapping("lotterygetbyid")
    public MyResult lotterygetById(@RequestParam String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            return lotteryService.getAwardListById(id, this.getUserId());
        }
        return json(HttpResponseEnums.BadRequest, null);
    }


    /**
     * 抽取商品
     *
     * @param id
     * @return
     */
    @GetMapping("getaward")
    public MyResult getAward(@RequestParam String id) throws Exception {
        if (StringUtils.isNotEmpty(id)) {
            if (id.equalsIgnoreCase("1111")) {
                return lotteryService.getAward_Temp(id, this.getUserId());
            } else {
                return lotteryService.getAwardById(id, this.getUserId());
            }
        }
        return json(HttpResponseEnums.BadRequest, null);
    }

    @GetMapping("refresh")
    public Object refresh(@RequestParam String id, @RequestParam Boolean o) {
        var lottery = (LotteryDTO) this.redisService.get(String.format("LOTTERY_%s", id));
        var item = (LotteryItemDTO) lottery.getLotteryItemList().stream().filter(e -> !e.getProductid().equalsIgnoreCase(IDUtils.empty())).findFirst().get();
        item.setPercent(BigDecimal.valueOf(0.5));
        lottery.setIsover(o);
        this.redisService.set(String.format("LOTTERY_%s", id), lottery);
        return JsonUtils.objectToJson(lottery);
    }


}
