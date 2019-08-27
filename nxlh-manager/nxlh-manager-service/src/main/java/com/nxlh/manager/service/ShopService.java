package com.nxlh.manager.service;

import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbShopMapper;
import com.nxlh.manager.model.dbo.TbShop;
import com.nxlh.manager.model.dto.SeckillDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.extend.ProductMinDTO;
import com.nxlh.manager.model.extend.ScoreProductMinDTO;
import com.nxlh.manager.model.extend.VipProductMinDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;


@CacheConfig(cacheNames = "shop", keyGenerator = "keyGenerator")
public interface ShopService extends BaseService<ShopDTO, TbShopMapper, TbShop> {

    /**
     * 根据名称获取商品
     *
     * @param name
     * @param count  数量限制，-1则不限制
     * @param status 上下架 -1不限制 1上架 0下架
     * @return
     */
    List<ShopDTO> getByName(String name, int count, int status);


    /**
     * 查询积分商品
     *
     * @param name   关键字
     * @param page   -1位不限制数量
     * @param status 上下线状态
     * @return
     */
    Pagination<ScoreProductMinDTO> getScoreShopByName(String name, PageParameter page, int status);

    Pagination<ShopDTO> getSaleShopByName(String name, PageParameter page, int status) throws Exception;

    /**
     * 查询出租商品
     *
     * @param name
     * @param type   soft_rent,machine_rent,rent
     * @param page
     * @param status
     * @return
     * @throws Exception
     */
    Pagination<ShopDTO> getRentShopByName(String name, String type, PageParameter page, int status) throws Exception;

    /**
     * 根据ID获取商品
     * Web使用
     *
     * @param id
     * @return
     */
    ShopDTO getShopInfoById(String id);


    /**
     * 根据Id获取商品，与上面无异
     * 小程序端使用
     *
     * @param shopid
     * @return
     */
    ShopDTO getShopInfo(String wxUserId, String shopid, String sid);

    /**
     * 添加或编辑商品信息(关联分类)
     *
     * @param shop
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean addOrUpdateShop(ShopDTO shop);


    /**
     * 获取新品
     *
     * @return
     */
//    @Cacheable
    List<ShopDTO> getNewShopes(PageParameter page);

    /**
     * 获取热门推荐
     *
     * @param page
     * @return
     */
//    @Cacheable
    List<ShopDTO> getHotShopes(PageParameter page);

    /**
     * 获取活动商品
     *
     * @return
     */
//    @Cacheable
    List<ShopDTO> getDiscountShopes(PageParameter page);


    /**
     * 根据分类获取商品列表
     *
     * @param cid
     * @return
     */
    @Cacheable()
    Pagination<ShopDTO> getPageByCategory(String cid, PageParameter page);


    /**
     * 商品上下架
     *
     * @param shopDTO
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean editStatus(ShopDTO shopDTO);


    /**
     * 获取积分商品
     *
     * @return
     */
    @Cacheable
    Pagination<ScoreProductMinDTO> getScoreShopes(PageParameter page);


    /**
     * 检测商品是否重复
     *
     * @param name
     */
    boolean checkSimilarName(String id, String name);


    @CacheEvict(allEntries = true)
    boolean shopDeleteById(String var1);


    /**
     * 根据活动 id获取对应的商品
     *
     * @return
     */
    List<VipProductMinDTO> getSecKillShops(String sid);

    /**
     * 获取特价商品
     *
     * @param id
     * @return
     */
    ShopDTO getVipShopById(String sid, String id);


}
