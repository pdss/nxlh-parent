package com.nxlh.minapp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.service.ShoppingCarService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@ApiController("/api/wx/car")
public class ShoppingCarController extends BaseController {

    @Reference
    private ShoppingCarService shoppingCarService;

    /**
     * 添加到购物车
     *
     * @return
     */
    @GetMapping("addtocar")
    public MyResult addToCar(@RequestParam String pid) throws UnAuthorizeException {
        if (StringUtils.isEmpty(pid)) {
            return MyResult.build(HttpResponseEnums.BadRequest, null);
        }
        return this.shoppingCarService.addToCar(this.getUserId(), pid);

    }


    /**
     * 更新车中某商品的数量
     *
     * @return
     */
    @GetMapping("setprocount")
    public MyResult plus(@RequestParam String id, @RequestParam Integer count) throws UnAuthorizeException {
        if (StringUtils.isEmpty(id) || count < 1) {
            return MyResult.build(HttpResponseEnums.BadRequest, null);
        }
        return this.shoppingCarService.setProductCount(this.getUserId(), id,count);
    }



    /**
     * 获取购物车
     *
     * @return
     */
    @GetMapping("getcars")
    public MyResult getCars() throws UnAuthorizeException {
        return this.shoppingCarService.getCars(this.getUserId());
    }


    /**
     * 移除商品
     *
     * @return
     */
    @GetMapping("remove")
    public MyResult removeShop(@RequestParam String id) throws UnAuthorizeException {
        if (StringUtils.isEmpty(id)) {
            return this.badreq("");
        }

        return this.shoppingCarService.removeShop(this.getUserId(), id);
    }


}
