package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.manager.mapper.TbWxuserRentshopMapper;
import com.nxlh.manager.model.dbo.TbWxuserRentshop;
import com.nxlh.manager.model.dto.WxUserRentShopDTO;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.service.WxUserRentShopService;
import lombok.extern.slf4j.Slf4j;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = WxUserRentShopService.class)
@Slf4j
public class WxUserRentShopServiceImp extends BaseDbCURDSServiceImpl<TbWxuserRentshopMapper, TbWxuserRentshop, WxUserRentShopDTO> implements WxUserRentShopService {


    @Override
    public WxUserRentShopDTO isExistValidRentByWxUserId(String userid) {
        var dbo = this.baseMapper.isExistValidRentByWxUserId(userid);
        return this.beanMap(dbo, this.currentDTOClass());
    }

    @Override
    public WxUserRentShopDTO getByOrderId(String orderId) {
        var dbo = this.baseMapper.getByOrderId(orderId);
        return this.beanMap(dbo, this.currentDTOClass());
    }

    @Override
    public int autoUpdateRentRecord() {
        //30天前
        var before = DateUtils.addDay(new Date(), -30);
        //找出30天前未归还的租赁记录
        var example = this.sqlBuilder().where(Sqls.custom()
                .andLessThanOrEqualTo("addtime", before)
                .andEqualTo("status", OrderEnums.RentOrderStatusEnum.wait.getValue()))
                .build();

        var model = new TbWxuserRentshop();
        model.setStatus(OrderEnums.RentOrderStatusEnum.buy.getValue());
        //更新这些记录为买断
        var count = this.baseMapper.updateByExampleSelective(model, example);
        this.log.info("租赁更新:" + count);
        return count;
    }

    @Override
    public List<WxUserRentShopDTO> getOverdue(Date date) {
        return this.baseMapper.getRentOverdue(date);
    }

    @Override
    public Long getRentRecordInDateTime(Date startTIme, String wxuserid, int protype) {
        return this.baseMapper.getRentRecordInDateTime(startTIme, wxuserid, protype);
    }

    @Override
    public List<WxUserRentShopDTO> getEndDateIsNullOrder() {
        return this.baseMapper.getEndDateIsNullOrder();
    }
}
