package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbVipShopMapper;
import com.nxlh.manager.model.dbo.TbVipShop;
import com.nxlh.manager.model.dto.VipShopDTO;

import java.util.List;

public interface VipShopService extends BaseService<VipShopDTO, TbVipShopMapper, TbVipShop> {

    /**
     * 上下架
     *
     * @param vipShopDTO
     * @return
     */
    boolean editStatus(VipShopDTO vipShopDTO);


//    List<VipShopDTO> queryAll();

    MyResult addOrUpdateVipShop(VipShopDTO vipShopDTO);


    /**
     * 更新商品库存
     * @param sid
     * @param proId
     * @param stock
     * @return
     */
    boolean updateStock(String sid,String proId,int stock);



}
