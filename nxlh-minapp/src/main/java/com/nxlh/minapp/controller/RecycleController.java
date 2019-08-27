package com.nxlh.minapp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.manager.model.dbo.TbRecycleProduct;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import com.nxlh.manager.service.RecycleProductService;
import com.nxlh.manager.service.WxUserRecycleService;
import com.nxlh.minapp.model.RecycleSendVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;

@ApiController("/api/wx/recycle")
public class RecycleController extends BaseController {



    @Reference
    @Lazy
    private RecycleProductService recycleProductService;

    @Reference
    @Lazy
    private WxUserRecycleService wxUserRecycleService;


    /**
     * 回收商品列表
     *
     * @return
     */
    @GetMapping("shop")
    public MyResult recycles(@RequestParam(defaultValue = "8") Integer pageSize) {
        var list = this.recycleProductService.page(new PageParameter(1, pageSize),null,null,1);
        return MyResult.ok(list);
    }

    /**
     * 创建工单
     *
     * @param recycleDTO
     * @return
     * @throws UnAuthorizeException
     */
    @PostMapping("order")
    public MyResult order(@RequestBody WxUserRecycleDTO recycleDTO) throws UnAuthorizeException {

        recycleDTO.setUserid(this.getUserId());
        var order = this.recycleProductService.order(recycleDTO);
        return MyResult.ok(order);
    }

    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id) {
        var shop = this.recycleProductService.getById(id);
        return MyResult.ok(shop);
    }


    @GetMapping("listbypage")
    public MyResult listbypage(@RequestParam String type, @RequestParam(defaultValue = "0") Integer pageIndex) throws UnAuthorizeException {
        var res = this.wxUserRecycleService.listByPage(new PageParameter(pageIndex, this.pageSize), type, this.getUserId());
        return MyResult.ok(res);
    }

    @GetMapping("getorder")
    public MyResult getOrder(@RequestParam String id) {
        var res = this.wxUserRecycleService.getById(id);
        return MyResult.ok(res);
    }


    /**
     * 寄送
     *
     * @param model
     * @return
     */
    @PostMapping("send")
    public MyResult send(@RequestBody RecycleSendVO model) {
        var res = this.wxUserRecycleService.send(model.getOrderid(), model.getExpress(), model.getExpressno(),model.getAccount(),model.getAccounttype());
        return MyResult.ok(res);
    }

}
