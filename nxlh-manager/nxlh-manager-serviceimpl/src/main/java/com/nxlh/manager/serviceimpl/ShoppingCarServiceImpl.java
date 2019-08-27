package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.mapper.TbShoppingcarMapper;
import com.nxlh.manager.model.dbo.TbShoppingcar;
import com.nxlh.manager.model.dto.ShoppingCarDTO;
import com.nxlh.manager.service.ShopService;
import com.nxlh.manager.service.ShoppingCarService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.nxlh.manager.amqp.queue.ShoppingCarQueue.SHOPPINGCAR_ADD;
import static com.nxlh.manager.amqp.queue.ShoppingCarQueue.SHOPPINGCAR_DELETE;
import static com.nxlh.manager.amqp.queue.ShoppingCarQueue.SHOPPINGCAR_UPDATE;
import static com.nxlh.manager.rediskey.Keys.SHOPPINGCAR_KEY;
import static java.util.stream.Collectors.toList;

@Service(interfaceClass = ShoppingCarService.class)
@Slf4j
public class ShoppingCarServiceImpl extends BaseDbCURDSServiceImpl<TbShoppingcarMapper, TbShoppingcar, ShoppingCarDTO> implements ShoppingCarService {


    @Autowired
    private ShopService shopService;

    //用户购物车缓存的有效期,30Min
    @Value("${nxlh.carRedisExpire}")
    private final long UserCarExpire = 10L;

    /**
     * 预加载数据
     *
     * @param wxUserId
     * @return
     */
    private List<ShoppingCarDTO> prepareFromRedis(String wxUserId) {

        String cacheKey = String.format(SHOPPINGCAR_KEY, wxUserId);
        //先从缓存中读取数据
        var cache = this.redisService.get(cacheKey);
        //如果缓存中没有数据，则从数据库读取
        if (cache == null) {
            String[] sort = {"addtime"};
            var dbo = this.baseMapper.getCarsByUserId(wxUserId);
            cache = this.beanListMap(dbo, this.currentDTOClass());
        }
        if (cache == null) {
            cache = new ArrayList<ShoppingCarDTO>();
        }
        return (List<ShoppingCarDTO>) cache;
    }


    @Override
    public MyResult addToCar(String wxUserId, String productId) {
        String cacheKey = String.format(SHOPPINGCAR_KEY, wxUserId);
        var cars = prepareFromRedis(wxUserId);

        var opt_car = ((List<ShoppingCarDTO>) cars).stream().filter(e -> e.getProductid().equals(productId)).findAny();

        if (opt_car.isPresent()) {
            opt_car.get().setCount(opt_car.get().getCount() + 1);
            this.rabbitTemplate.convertAndSend(SHOPPINGCAR_UPDATE, opt_car.get(), new CorrelationData(IDUtils.genUUID()));
        } else {
            var car = new ShoppingCarDTO();
            car.setId(IDUtils.genUUID());
            car.setAddtime(DateUtils.now());
            car.setCount(1);
            car.setProductid(productId);
            car.setWxuserid(wxUserId);
            ((List<ShoppingCarDTO>) cars).add(car);
            this.rabbitTemplate.convertAndSend(SHOPPINGCAR_ADD, car, new CorrelationData(IDUtils.genUUID()));
        }
        this.redisService.set(cacheKey, cars, UserCarExpire, TimeUnit.DAYS);

        return MyResult.ok();
    }


    @Override
    public MyResult setProductCount(String wxUserId, String productId, Integer count) {
        String cacheKey = String.format(SHOPPINGCAR_KEY, wxUserId);
        var cars = prepareFromRedis(wxUserId);

        var opt_car = ((List<ShoppingCarDTO>) cars).stream().filter(e -> e.getProductid().equals(productId)).findAny();
        if (!opt_car.isPresent()) {
            return MyResult.build(HttpResponseEnums.BadRequest, null);
        } else {
            var car = opt_car.get();
            car.setCount(count);
            this.rabbitTemplate.convertAndSend(SHOPPINGCAR_UPDATE, car, new CorrelationData(IDUtils.genUUID()));
        }
        this.redisService.set(cacheKey, cars, UserCarExpire, TimeUnit.MINUTES);
        return MyResult.ok(count);
    }

    @Override
//    @RedisExpire(expiress = 30)
    public MyResult getCars(String wxuserid) {
        String cacheKey = String.format(SHOPPINGCAR_KEY, wxuserid);
        var cars = this.prepareFromRedis(wxuserid);
        if (CollectionUtils.isNotEmpty(cars)) {
            var proIds = cars.stream().map(e -> e.getProductid()).collect(toList());
            var shops = this.shopService.list(this.sqlBuilder().where(Sqls.custom()
                    .andIn("id", proIds)
                    .andEqualTo("isdelete", 0)).build());
            shops.forEach(e -> {
                var car = cars.stream().filter(s -> s.getProductid().equals(e.getId())).findAny();
                car.get().setProductInfo(e);

            });
        }
        this.redisService.set(cacheKey, cars, UserCarExpire, TimeUnit.MINUTES);
        return MyResult.ok(cars);
    }

    public MyResult removeShop(String wxuserid, String productId) {

        return this.removeShops(wxuserid, List.of(productId));
    }

    @Override
    public MyResult removeShops(String wxuserid, List<String> productids) {
        String cacheKey = String.format(SHOPPINGCAR_KEY, wxuserid);
        var cars = this.prepareFromRedis(wxuserid);
        //购物车中的商品id
//        var opt_cars = ((List<ShoppingCarDTO>) cars).stream().filter(e -> productids.indexOf(e.getProductid()) != -1).map(e->e.getProductid()).collect(toList());
//        if (CollectionUtils.isNotEmpty(opt_cars)) {
//           cars = cars.stream().filter(e->opt_cars.)
//        }
        //保留的购物车信息
        cars = cars.stream().filter(e -> productids.indexOf(e.getProductid()) == -1).collect(Collectors.toList());

        this.redisService.set(cacheKey, cars, UserCarExpire, TimeUnit.MINUTES);

        this.rabbitTemplate.convertAndSend(SHOPPINGCAR_DELETE, new HashMap<String, Object>() {{
            put("wxuserid", wxuserid);
            put("productids", productids);
        }}, new CorrelationData(IDUtils.genUUID()));
        return MyResult.ok();
    }

    @Override
    public boolean removeShopByShopId(String shopid) {
        Example example = Example.builder(TbShoppingcar.class).where(Sqls.custom().andEqualTo("productid", shopid)).build();
        return this.baseMapper.deleteByExample(example) > 0 ? true : false;
    }
}
