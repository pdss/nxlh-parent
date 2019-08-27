package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbVipShopMapper;
import com.nxlh.manager.model.dbo.TbVipShop;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.dto.VipShopDTO;
import com.nxlh.manager.service.ShopService;
import com.nxlh.manager.service.VipShopService;
import jodd.util.CollectionUtil;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service(interfaceClass = VipShopService.class)
public class VipShopServiceImpl extends BaseDbCURDSServiceImpl<TbVipShopMapper, TbVipShop, VipShopDTO> implements VipShopService {


    //秒杀商品KEY
    private final String MS_PRODUCTS_KEY = "MS_PRODUCTS_%s";

    //秒杀商品库存占用
    private final String MS_SHOP_STOCK_SET_KEY = "MS_SHOP_STOCK_SET_%s";

    @Autowired
    private ShopService shopService;


    @Override
    public Pagination<VipShopDTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        String s = example.getOredCriteria().get(1).getCriteria().get(0).getValue().toString();
        List<VipShopDTO> tbVipShop = this.baseMapper.queryPage(s);

        //当前活动中所有占用数
        var stocks = (Map<String, Map<String, Date>>) this.redisService.get(String.format(MS_SHOP_STOCK_SET_KEY, s));
        if(!CollectionUtils.isEmpty(tbVipShop) && MapUtils.isNotEmpty(stocks)){
            tbVipShop.forEach(e -> {
                //对应的商品
                var stockMap = stocks.get(e.getShopid());
                if (MapUtils.isNotEmpty(stockMap)) {
                    var now = DateUtils.now();
                    var validStocks = new HashMap<String, Date>();
                    //过滤无效占用
                    stockMap.entrySet().forEach(entry -> {
                        if (now.before(entry.getValue())) {
                            validStocks.put(entry.getKey(), entry.getValue());
                        }
                    });
                    e.setStockInUsing(validStocks.size());
                    stocks.put(e.getShopid(), validStocks);
                    //更新下缓存
                    this.redisService.set(String.format(MS_SHOP_STOCK_SET_KEY, s), stocks);
                }
            });
        }

        PageInfo<VipShopDTO> pageInfo = new PageInfo<VipShopDTO>(tbVipShop);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

    @Override
    public boolean editStatus(VipShopDTO vipShopDTO) {
        TbVipShop tbVipShop = this.baseMapper.selectByPrimaryKey(vipShopDTO.getId());
        tbVipShop.setStatus(vipShopDTO.getStatus());
        this.baseMapper.updateByPrimaryKey(vipShopDTO);

        var up = vipShopDTO.getStatus() == 1;

        var ms_products = (List<ShopDTO>) this.redisService.get(String.format(MS_PRODUCTS_KEY, vipShopDTO.getActivityid()));
        if (CollectionUtils.isEmpty(ms_products)) {
            ms_products = new ArrayList<>();
        }
        if (up) {
            var shop = this.shopService.getVipShopById(vipShopDTO.getActivityid(), vipShopDTO.getShopid());
            //上架商品时，加入缓存
            var exists = ms_products.stream().filter(e -> e.getId().equalsIgnoreCase(vipShopDTO.getShopid())).count();
            if (exists == 0) {
                ms_products.add(shop);
            }
        } else {
            //下架，从缓存剔除
            ms_products = ms_products.stream().filter(e -> !e.getId().equalsIgnoreCase(vipShopDTO.getShopid())).collect(toList());
        }

        //更新缓存
        this.redisService.set(String.format(MS_PRODUCTS_KEY, vipShopDTO.getActivityid()), ms_products);
        return true;

    }

    @Override
    public boolean deleteById(String id) {
        var shop = this.getById(id);
        super.deleteById(id);


        var ms_products = (List<ShopDTO>) this.redisService.get(String.format(MS_PRODUCTS_KEY, shop.getActivityid()));
        if (!CollectionUtils.isEmpty(ms_products)) {
            ms_products = ms_products.stream().filter(e -> !e.getId().equalsIgnoreCase(id)).collect(toList());
        }
        //更新缓存
        this.redisService.set(String.format(MS_PRODUCTS_KEY, shop.getActivityid()), ms_products);
        return true;
    }

//    @Override
//    public List<VipShopDTO> queryAll() {
//        return this.baseMapper.queryPage();
//    }

    @Override
    public MyResult addOrUpdateVipShop(VipShopDTO vipShopDTO) {
        if (null == vipShopDTO) {
            return MyResult.forb("无效参数");
        } else {
            if (StringUtils.isEmpty(vipShopDTO.getId())) {
//                var filter = new HashMap<String,Object>() {{
//                    put("activityid", vipShopDTO.getActivityid());
//                    put("shopid", vipShopDTO.getShopid());
//                    put("isdelete", 0);
//                }};
                var example = this.sqlBuilder()
                        .where(Sqls.custom()
                                .andEqualTo("activityid", vipShopDTO.getActivityid())
                                .andEqualTo("shopid", vipShopDTO.getShopid())
                                .andEqualTo("isdelete", 0)).build();

                if (this.count(example) > 0) {
                    return MyResult.forb("已存在相同的商品");
                }

                this.add(vipShopDTO);
            } else {
                this.updateById(vipShopDTO);
            }

            var ms_products = (List<ShopDTO>) this.redisService.get(String.format(MS_PRODUCTS_KEY, vipShopDTO.getActivityid()));
            if (CollectionUtils.isEmpty(ms_products)) {
                ms_products = new ArrayList<>();
            }
            var shop = this.shopService.getVipShopById(vipShopDTO.getActivityid(), vipShopDTO.getShopid());
            //上架商品时，加入缓存
            var others = ms_products.stream().filter(e -> !e.getId().equalsIgnoreCase(vipShopDTO.getShopid())).collect(toList());

            //更新成功并且为上架商品更新缓存
            if (vipShopDTO.getStatus() == 1) {
                others.add(shop);
            }
            this.redisService.set(String.format(MS_PRODUCTS_KEY, vipShopDTO.getActivityid()), others);
            return MyResult.ok();
        }


    }

    @Override
    public boolean updateStock(String sid, String proId, int stock) {
        var example = this.sqlBuilder().where(Sqls.custom()
                .andEqualTo("shopid", proId)
                .andEqualTo("activityid", sid)
                .andEqualTo("isdelete", 0)
                .andEqualTo("status", 1)).build();
        var dto = new VipShopDTO();
        dto.setStockcount(stock);
        this.baseMapper.updateByExampleSelective(dto, example);
        return true;
    }

}
