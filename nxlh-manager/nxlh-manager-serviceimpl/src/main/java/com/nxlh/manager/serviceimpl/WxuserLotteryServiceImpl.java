package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbWxuserLotteryMapper;
import com.nxlh.manager.model.dbo.TbWxuserLottery;
import com.nxlh.manager.model.dto.WxuserLotteryDTO;
import com.nxlh.manager.service.WxuserLotteryService;

import java.util.List;

@Service(interfaceClass = WxuserLotteryService.class)
public class WxuserLotteryServiceImpl extends BaseDbCURDSServiceImpl<TbWxuserLotteryMapper, TbWxuserLottery, WxuserLotteryDTO> implements WxuserLotteryService {


    @Override
    public List<WxuserLotteryDTO> queryAllByLotteryId(String lotteryid) {
        return this.baseMapper.queryAllByLotteryId(lotteryid);
    }

    @Override
    public boolean exchange(String id) {
        TbWxuserLottery tbWxuserLottery = this.baseMapper.selectByPrimaryKey(id);
        tbWxuserLottery.setStatus(1);
        int i = this.baseMapper.updateByPrimaryKey(tbWxuserLottery);
        return i > 0 ? true : false;
    }

}
