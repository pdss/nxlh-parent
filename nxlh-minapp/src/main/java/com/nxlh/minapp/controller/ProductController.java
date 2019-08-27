package com.nxlh.minapp.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.model.extend.ProductMinDTO;
import com.nxlh.manager.service.*;
import com.nxlh.minapp.model.ProductByCategoryVO;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

@ApiController("/api/wx/product")
public class ProductController extends BaseController {

    @Reference
    @Lazy
    private ShopService shopService;


    @Reference
    @Lazy
    private SeckillService seckillService;

    @Reference
    @Lazy
    private ModulesProductsService modulesProductsService;


    /**
     * 根据分类获取商品列表
     *
     * @return
     */
    @PostMapping("getbycid")
    public MyResult getPageByCategory(@RequestBody ProductByCategoryVO request) {
        var products = this.shopService.getPageByCategory(request.getCid(), new PageParameter(request.getPageIndex()));
        return ok(products);
    }


    /**
     * 商品搜索
     *
     * @return
     */
    @GetMapping("search")
    public MyResult search(@RequestParam String name, @RequestParam(defaultValue = "1") Integer pageIndex) throws Exception {
        var shops = this.shopService.getSaleShopByName(name, new PageParameter(pageIndex, this.pageSize), 1);
        var products = this.beanListMap(shops.getList(), ProductMinDTO.class);
        return ok(new Pagination<ProductMinDTO>(shops, products));
    }

    /**
     * 租赁商品搜索
     *
     * @param name
     * @param pageIndex
     * @return
     * @throws Exception
     */
    @GetMapping("searchbyrent")
    public MyResult searchbyrent(@RequestParam String name, @RequestParam(defaultValue = "1") Integer pageIndex, @RequestParam String type) throws Exception {
        var shops = this.shopService.getRentShopByName(name, type, new PageParameter(pageIndex, this.pageSize), 1);
        var products = this.beanListMap(shops.getList(), ProductMinDTO.class);
        return ok(new Pagination<ProductMinDTO>(shops, products));
    }


    /**
     * 积分商品搜索
     *
     * @return
     */
    @GetMapping("searchscoreshop")
    public MyResult searchScoreShop(@RequestParam String name, @RequestParam(defaultValue = "1") Integer pageIndex) {
        var shops = this.shopService.getScoreShopByName(name, new PageParameter(pageIndex, this.pageSize), 1);
        return ok(shops);
    }


    /**
     * 根据ID获取详情，商品详情页
     *
     * @param sid 活动id,可选
     * @return
     */
    @GetMapping("getbyid")
    public MyResult getById(@RequestParam String id, @RequestParam(required = false) String sid) throws UnAuthorizeException {
        var pro = this.shopService.getShopInfo(this.getUserId(), id, sid);
        return ok(pro);
    }

    /**
     * 获取推荐商品
     *
     * @return
     */
    @GetMapping("recommends")
    public MyResult getRecommends(@RequestParam(defaultValue = "4") Integer count) {
        if (count < 1) {
            count = 4;
        }
        var pros = this.shopService.getHotShopes(new PageParameter(0, count));
        return ok(pros);
    }


    /**
     * 获取积分商品
     *
     * @return
     */
//    @GetMapping("scoreproducts")
//    public MyResult getScoreProducts(@RequestParam String name,@RequestParam(defaultValue = "0") Integer pageIndex) {
//        var page = this.shopService.getScoreShopByName(name,new PageParameter(pageIndex),1);
//        return ok(page);
//    }
    @GetMapping("scoreproducts")
    public MyResult getScoreProducts(@RequestParam(defaultValue = "0") Integer pageIndex) {
        var page = this.shopService.getScoreShopes(new PageParameter(pageIndex));
        return ok(page);
    }


    /**
     * 获取特价商品
     *
     * @return
     */
    @GetMapping("getseckillshops")
    public MyResult getSeckillShops(@RequestParam String sid) throws UnAuthorizeException {
        var shops = this.shopService.getSecKillShops(sid);
        return MyResult.ok(shops);
    }


    @GetMapping("checkms")
    public MyResult checkSecKillStatus(@RequestParam String sid) {
        var result = this.seckillService.checkStatus(sid);
        return MyResult.ok(result);
    }

    @GetMapping("getbymoduleid")
    public MyResult getbymoduleid(@RequestParam String moduleid, @RequestParam(defaultValue = "1") Integer pageIndex) {
        var list = this.modulesProductsService.page(new PageParameter(pageIndex, 20), new HashMap<String, Object>() {{
            put("moduleid", moduleid);
            put("isdelete", 0);
        }}, new String[]{"sort"}, 1);
        return MyResult.ok(list);
    }

    /**
     * 搜索商品，针对租赁
     *
     * @param name      商品名
     * @param type      商品类型
     * @param pageIndex
     * @return
     * @throws Exception
     */
    @GetMapping("searchbytype")
    public MyResult searchbytype(@RequestParam String name, @RequestParam String type, @RequestParam(defaultValue = "1") Integer pageIndex) throws Exception {
        var shops = this.shopService.getRentShopByName(name, type, new PageParameter(pageIndex, this.pageSize), 1);
        var products = this.beanListMap(shops.getList(), ProductMinDTO.class);
        return ok(new Pagination<ProductMinDTO>(shops, products));
    }

}
