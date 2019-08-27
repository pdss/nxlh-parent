package com.nxlh.manager.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.shop.ShopQueryVO;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import com.nxlh.manager.service.ShopService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApiController("/api/web/shop")
public class ShopController extends BaseController {

    @Reference
    private ShopService shopService;


    @GetMapping("listbypage")
    public MyResult listByPage(Optional<ShopQueryVO> queryVO) throws Exception {
        var page = this.makePage(queryVO);
        if (StringUtils.isNotEmpty(queryVO.get().getShopname())) {
            Map<String, Object> map = new HashMap<>();
            map.put("shopname", queryVO.get().getShopname());
            var result = this.shopService.query(page, map,new String[]{"sort"}, new Integer[]{0});
            return ok(result);
        } else {
            var result = this.shopService.page(page, null, new String[]{"sort"}, 0);
            return ok(result);
        }
    }

    @PostMapping("update")
    public MyResult update(@Valid @RequestBody ShopDTO shopDTO, BindingResult result) {

        if (result.hasErrors()) {
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "商品名超过80个字符过长", null);
        }
        boolean b = this.shopService.checkSimilarName(shopDTO.getId(), shopDTO.getShopname());
        if (b) {
            var updateResult = this.shopService.addOrUpdateShop(shopDTO);
            return MyResult.build(updateResult ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
        }
        return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "商品名重复");
    }

    @GetMapping("delete")
    public MyResult delete(@RequestParam String id) {
        var result = this.shopService.shopDeleteById(id);
        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var obj = this.shopService.getShopInfoById(id);
        return this.ok(obj);
    }

    @PostMapping("getbyname")
    public MyResult getByName(@RequestBody String name) throws Exception {
//        var list = this.shopService.getByName(name, 10, 1);
//        return this.ok(list);
        var list = this.shopService.getSaleShopByName(name, new PageParameter(1,10), 1);
        return this.ok(list.getList());
    }

    @PostMapping("getlotteryitembyname")
    public MyResult getLotteryItemByName(@RequestBody String name) {
        List<ShopDTO> list = this.shopService.getByName(name, 5, -1);
        return this.ok(list);
    }

    @PostMapping("editstatus")
    public MyResult editstatus(@RequestBody ShopDTO shopDTO) {
        var result = this.shopService.editStatus(shopDTO);

        return MyResult.build(result ? HttpResponseEnums.Ok : HttpResponseEnums.InternalServerError, null);
    }

    @PostMapping("checkname")
    public MyResult checkName(@RequestBody ShopDTO shopDTO) {
        if (shopDTO != null && StringUtils.isNotEmpty(shopDTO.getShopname())) {
            var result = this.shopService.checkSimilarName(shopDTO.getId(), shopDTO.getShopname());
            return result ? MyResult.ok() : MyResult.build(HttpResponseEnums.BadRequest.getValue(), "商品名重复");

        }
        return MyResult.build(HttpResponseEnums.InternalServerError, null);
    }


}
