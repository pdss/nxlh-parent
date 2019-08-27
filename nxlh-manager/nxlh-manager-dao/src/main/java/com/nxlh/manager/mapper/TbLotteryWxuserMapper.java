package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbLotteryWxuser;
import com.nxlh.manager.model.dto.LotteryWxUserDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbLotteryWxuserMapper extends Mapper<TbLotteryWxuser> {

    /**
     * 获取抽奖的参与用户信息
     * @param lotteryId
     * @return
     */
    List<LotteryWxUserDTO> getLotteryWithJoinUsers(String lotteryId);
}