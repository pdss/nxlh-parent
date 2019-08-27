package com.nxlh.manager.service;

import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbCouponsMapper;
import com.nxlh.manager.model.dbo.TbCoupons;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

public interface CouponsService extends BaseService<CouponsDTO, TbCouponsMapper, TbCoupons> {

    Pagination<CouponsDTO> page(PageParameter var1, Map<String, Object> parameters, String[] orderby, Integer isAsc);

    Pagination<CouponsDTO> page(PageParameter page, Example example);

    //根据优惠卷名称查找
    List<CouponsDTO> getByName(String name);

    /**
     * 添加优惠券
     *
     * @param couponsDTO
     * @return
     */
    boolean addCoupons(CouponsDTO couponsDTO);


    /**
     * 根据id删除优惠券
     *
     * @param id
     * @return
     */
    boolean delCouponByid(String id);


}
