package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbLotteryMapper;
import com.nxlh.manager.mapper.TbLotteryWxuserMapper;
import com.nxlh.manager.model.dbo.TbLottery;
import com.nxlh.manager.model.dbo.TbLotteryWxuser;
import com.nxlh.manager.model.dto.LotteryDTO;
import com.nxlh.manager.model.dto.LotteryWxUserDTO;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

public interface LotteryWxUserService extends BaseService<LotteryWxUserDTO, TbLotteryWxuserMapper, TbLotteryWxuser> {


    /**
     * 获取抽奖的参与用户信息
     * @param lotteryId
     * @return
     */
    List<LotteryWxUserDTO> getLotteryWithJoinUsers(String lotteryId);

}
