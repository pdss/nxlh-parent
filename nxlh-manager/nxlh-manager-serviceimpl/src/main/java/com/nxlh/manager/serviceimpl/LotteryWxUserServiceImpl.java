package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbLotteryWxuserMapper;
import com.nxlh.manager.model.dbo.TbLotteryWxuser;
import com.nxlh.manager.model.dto.LotteryWxUserDTO;
import com.nxlh.manager.service.LotteryWxUserService;

import java.util.List;

@Service(interfaceClass = LotteryWxUserService.class)
public class LotteryWxUserServiceImpl extends BaseDbCURDSServiceImpl<TbLotteryWxuserMapper, TbLotteryWxuser, LotteryWxUserDTO> implements LotteryWxUserService {


    @Override
    public List<LotteryWxUserDTO> getLotteryWithJoinUsers(String lotteryId) {
        return this.baseMapper.getLotteryWithJoinUsers(lotteryId);
    }
}
