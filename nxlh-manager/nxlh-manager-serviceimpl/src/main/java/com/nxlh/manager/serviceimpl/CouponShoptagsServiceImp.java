package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbCouponShoptagsMapper;
import com.nxlh.manager.model.dbo.TbCouponShoptags;
import com.nxlh.manager.model.dto.CouponShoptagsDTO;
import com.nxlh.manager.service.CouponShoptagsService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.ArrayList;
import java.util.List;


@Service(interfaceClass = CouponShoptagsService.class)
public class CouponShoptagsServiceImp extends BaseDbCURDSServiceImpl<TbCouponShoptagsMapper, TbCouponShoptags, CouponShoptagsDTO> implements CouponShoptagsService {


    @Override
    public boolean addTags(String couponid, List<String> tagids) {
        List<TbCouponShoptags> list = new ArrayList<>();
        tagids.stream().forEach(e -> {
            TbCouponShoptags model = new TbCouponShoptags();
            model.setCouponid(couponid);
            model.setTagid(e);
            list.add(model);
        });
        boolean result = this.transactionUtils.transact((a) -> {

            this.baseMapper.insetCouponShoptagsList(list);

        });

        return result;
    }

    @Override
    public boolean delByCouponId(String cid) {
        boolean result = this.transactionUtils.transact((a) -> {
            Example example = Example.builder(TbCouponShoptags.class).where(Sqls.custom().andEqualTo("couponid", cid)).build();
            this.baseMapper.deleteByExample(example);
        });

        return result;
    }
}
