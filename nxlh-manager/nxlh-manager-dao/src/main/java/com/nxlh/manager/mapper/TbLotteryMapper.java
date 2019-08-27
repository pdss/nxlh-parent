package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbLottery;
import com.nxlh.manager.model.dto.LotteryDTO;
import com.thoughtworks.xstream.mapper.Mapper;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbLotteryMapper extends Mapper<TbLottery> {

    /**
     * 根据id查询活动表并关联关联表
     *
     * @param id
     * @return
     */
    LotteryDTO queryInfoById(String id);

    /**
     * 查询用户参与活动的统计页
     *
     * @return
     */
    List<LotteryDTO> queryWxuserLotteryCount();


    /**
     * 获取指定用户可参与的活动，针对手动指定参与用户
     * @param wxuserId
     * @return
     */
    List<LotteryDTO> getLotteriesByJoinUserId(String wxuserId);
}