package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbShop;
import com.nxlh.manager.model.dto.ShopDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbShopMapper extends Mapper<TbShop> {
    /**
     * 获取商品详情，包括分类信息
     * @param id
     * @return
     */
    TbShop queryByid(String id);

    /**
     * 根据分类ID获取商品列表
     * @param cid
     * @return
     */
    List<TbShop> listByCategory(@Param("cid") String cid);


    /**
     * 获取活动的商品
     * @return
     */
    List<ShopDTO> getVipShopsBySId(String id);

    /**
     * 获取特价商品信息
     * @param shopid
     * @return
     */
    ShopDTO getShopWithVipById(@Param("sid") String sid,@Param("shopid") String shopid);

}