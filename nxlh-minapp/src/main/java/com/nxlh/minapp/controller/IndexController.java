package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.model.dto.IndexCategroyDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.VipConfigJsonModel;
import com.nxlh.manager.service.*;
import com.nxlh.minapp.model.IndexModuleVO;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static java.util.stream.Collectors.toList;

@ApiController("/api/wx/index")
public class IndexController extends BaseController {

    @Reference
    @Lazy
    private ShopService shopService;

    @Reference
    @Lazy
    private BannerService bannerService;


    @Reference
    @Lazy
    private OrderService orderService;

    @Reference
    @Lazy
    private MinappConfigService minappConfigService;

    @Reference
    @Lazy
    private LotteryService lotteryService;

    @Reference
    @Lazy
    private UserNotificationService userNotificationService;

    @Reference
    @Lazy
    private IndexcategoryService indexcategoryService;

    @Reference
    @Lazy
    private IndexmodulesService indexmodulesService;

    /**
     * 首页所有分类的数据
     *
     * @return
     */
    @GetMapping("index")
    public MyResult index() throws Exception {

        var cates = this.indexcategoryService.list(null, ObjectUtils.toArray("sort"), 1);

        var modules = this.indexmodulesService.indexModules();

        //新品
//        var newProducts = this.shopService.getNewShopes(new PageParameter(0, 6));

        //banners
        var banners = this.bannerService.getValidBanners();

        //活动商品
//        var discountProducts = this.shopService.getDiscountShopes(new PageParameter(0, 6));
        //热门推荐
//        var hotProducts = this.shopService.getHotShopes(new PageParameter(0, 4));


        var combin = new HashMap<String, Object>() {
            {
                put("categories", cates);
                put("banners", banners);

//                put("news", new IndexModuleVO("新品上市", new ArrayList<>() {{
//                    if (!CollectionUtils.isEmpty(newProducts)) {
//                        addAll(newProducts);
//                    }
//                }}));
//                put("discounts", new IndexModuleVO("活动专区", new ArrayList<>() {{
//                    if (!CollectionUtils.isEmpty(discountProducts)) {
//                        addAll(discountProducts);
//                    }
//                }}));
//                put("recommends", new IndexModuleVO("热门推荐", new ArrayList<>() {{
//                    if (!CollectionUtils.isEmpty(hotProducts)) {
//                        addAll(hotProducts);
//                    }
//                }}));
            }
        };
         modules.forEach((k,v)->{
              combin.put(k,v);

         });

        return ok(combin);

    }

    /**
     * 我的页面，数字标记
     *
     * @return
     */
    @GetMapping("summary")
    public MyResult summary() throws UnAuthorizeException {

        var orders = this.orderService.getCountGroupByStatus(this.getUserId());
        return ok(orders);
    }


    /**
     * 获取主题背景
     *
     * @return
     */
    @GetMapping("themebg")
    public MyResult themeBg() {
        var image = this.minappConfigService.getThemeBgImage();
        return ok(image);
    }

    /**
     * 抽奖活动
     *
     * @return
     */
    @GetMapping("lottery")
    public MyResult lotteries() throws UnAuthorizeException {
        var active = this.lotteryService.getActivedByUser(this.getUserId());
        return MyResult.ok(active);
    }


    /**
     * 消息通知com.nxlh.manager.model.dto.LotteryDTO
     *
     * @return
     */
    @GetMapping("notis")
    public MyResult notifications(@RequestParam(defaultValue = "1") Integer pageIndex) throws UnAuthorizeException {
        var result = this.userNotificationService.getUserNotis(new PageParameter(pageIndex), this.getUserId());
        return MyResult.ok(result);
    }


    @GetMapping("indexcategory")
    public MyResult indexCategory(@RequestParam String type, @RequestParam(required = false) String sid) {
        List<ShopDTO> shopDTOS = new ArrayList<>();
        var page = new PageParameter(1, 100);
        switch (type) {
            case "new":
                shopDTOS = this.shopService.getNewShopes(page);
                break;
            case "discount":
                shopDTOS = this.shopService.getDiscountShopes(page);
                break;
            case "recommends":
                shopDTOS = this.shopService.getHotShopes(page);
                break;

            default:
                break;
        }
        return MyResult.ok(shopDTOS);
    }
}
