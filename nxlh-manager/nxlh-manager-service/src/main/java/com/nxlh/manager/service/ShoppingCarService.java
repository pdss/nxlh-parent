package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbShoppingcarMapper;
import com.nxlh.manager.mapper.TbWxuserAddressMapper;
import com.nxlh.manager.model.dbo.TbShoppingcar;
import com.nxlh.manager.model.dbo.TbWxuserAddress;
import com.nxlh.manager.model.dto.ShoppingCarDTO;
import com.nxlh.manager.model.dto.WxUserAddressDTO;

import java.util.List;

public interface ShoppingCarService extends BaseService<ShoppingCarDTO, TbShoppingcarMapper, TbShoppingcar> {


    /**
     * 添加到购物车
     *
     * @param wxUserId
     * @param productId
     * @return
     */
    MyResult addToCar(String wxUserId, String productId);


    /**
     * 更新车中某商品的数量
     *
     * @return
     */
    MyResult setProductCount(String wxUserId, String productId, Integer count);


    /**
     * 获取微信用户的购物车
     *
     * @param wxuserid
     * @return
     */
    MyResult getCars(String wxuserid);


    /**
     * 从购物车中移除商品
     *
     * @param wxuserid
     * @param productId
     * @return
     */
    MyResult removeShop(String wxuserid, String productId);

    MyResult removeShops(String wxuserid, List<String> productids);

    boolean removeShopByShopId(String shopid);

}
