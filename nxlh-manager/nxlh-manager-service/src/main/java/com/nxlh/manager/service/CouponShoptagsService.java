package com.nxlh.manager.service;


import com.nxlh.manager.mapper.TbCouponShoptagsMapper;
import com.nxlh.manager.model.dbo.TbCouponShoptags;
import com.nxlh.manager.model.dto.CouponShoptagsDTO;

import java.util.List;

public interface CouponShoptagsService extends BaseService<CouponShoptagsDTO, TbCouponShoptagsMapper, TbCouponShoptags> {

    /**
     * 添加优惠券优惠系列的关联表
     *
     * @param couponid
     * @param tagids
     * @return
     */
    boolean addTags(String couponid, List<String> tagids);


    /**
     * 根据优惠券id删除关联记录
     * @param cid
     * @return
     */
    boolean delByCouponId(String cid);
}
