package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbWxuserLotteryMapper;
import com.nxlh.manager.model.dbo.TbWxuserLottery;
import com.nxlh.manager.model.dto.WxuserLotteryDTO;

import java.util.List;

public interface WxuserLotteryService extends BaseService<WxuserLotteryDTO, TbWxuserLotteryMapper, TbWxuserLottery> {



    /**
     * 根据活动id查询活动记录
     *
     * @param lotteryid
     * @return
     */
    List<WxuserLotteryDTO> queryAllByLotteryId(String lotteryid);


    /**
     * 兑换商品
     *
     * @param id
     * @return
     */
    boolean exchange(String id);
}
