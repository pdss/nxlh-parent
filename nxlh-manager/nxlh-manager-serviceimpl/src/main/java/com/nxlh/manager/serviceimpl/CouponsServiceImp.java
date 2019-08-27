package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.mapper.TbCouponsMapper;
import com.nxlh.manager.model.dbo.TbCoupons;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.service.CouponShoptagsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.nxlh.manager.service.CouponsService;
import tk.mybatis.mapper.entity.Example;

import java.util.*;
import java.util.stream.Collectors;

@Service(interfaceClass = CouponsService.class)
public class CouponsServiceImp extends BaseDbCURDSServiceImpl<TbCouponsMapper, TbCoupons, CouponsDTO> implements CouponsService {

    @Autowired
    private CouponShoptagsService couponShoptagsService;

    @Override
    public Pagination<CouponsDTO> page(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = this.columnsMapToSqlBuilder(parameters);
        if (orderby.length > 0) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return page(page, example.build());
    }

    @Override
    public Pagination<CouponsDTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<CouponsDTO> tbCoupons = this.baseMapper.queryAll();
        PageInfo<CouponsDTO> pageInfo = new PageInfo<CouponsDTO>(tbCoupons);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

    /**
     * 根据优惠卷名称查找前5条优惠券
     *
     * @param name
     * @return
     */
    @Override
    public List<CouponsDTO> getByName(String name) {
        if (!("".equals(name.trim())) && name != null) {
            Example example = new Example(TbCoupons.class);
            example.createCriteria().andLike("title", "%" + name + "%").andEqualTo("isdelete", 0);
            PageHelper.startPage(0, 5);
            List<TbCoupons> tbCategories = this.baseMapper.selectByExample(example);
            return this.beanListMap(tbCategories, this.currentDTOClass());
        }

        return null;
    }


    @Override
    public boolean addCoupons(CouponsDTO couponsDTO) {
        boolean result = this.transactionUtils.transact((a) -> {
            couponsDTO.setId(IDUtils.genUUID());
            couponsDTO.setIsdelete(0);
            couponsDTO.setAddtime(new Date());
            this.add(couponsDTO);
            if (couponsDTO.getTagList().size() > 0) {
                List<String> collect = couponsDTO.getTagList().stream().map(e -> e.getId()).collect(Collectors.toList());
                this.couponShoptagsService.addTags(couponsDTO.getId(), collect);
            }
        });

        return result;
    }

    @Override
    public boolean delCouponByid(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.deleteById(id);
            this.couponShoptagsService.delByCouponId(id);
        });

        return result;
    }

}
