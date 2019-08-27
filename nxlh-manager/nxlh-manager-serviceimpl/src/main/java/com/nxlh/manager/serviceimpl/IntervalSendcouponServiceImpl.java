package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbIntervalSendcouponMapper;
import com.nxlh.manager.model.dbo.TbIntervalSendcoupon;
import com.nxlh.manager.model.dto.IntervalSendcouponDTO;
import com.nxlh.manager.service.IntervalSendcouponService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service(interfaceClass = IntervalSendcouponService.class)
public class IntervalSendcouponServiceImpl extends BaseDbCURDSServiceImpl<TbIntervalSendcouponMapper, TbIntervalSendcoupon, IntervalSendcouponDTO> implements IntervalSendcouponService {

    @Override
    public Pagination<IntervalSendcouponDTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<IntervalSendcouponDTO> tbIntervalSendCoupon = this.baseMapper.selectBypage();
        PageInfo<IntervalSendcouponDTO> pageInfo = new PageInfo<IntervalSendcouponDTO>(tbIntervalSendCoupon);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

}
