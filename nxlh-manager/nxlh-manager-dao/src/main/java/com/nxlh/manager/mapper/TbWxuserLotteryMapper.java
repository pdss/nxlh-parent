package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbWxuserLottery;
import com.nxlh.manager.model.dto.WxuserLotteryDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbWxuserLotteryMapper extends Mapper<TbWxuserLottery> {


    /**
     * 插入用户的抽奖记录
     *
     * @param wxuserid
     * @param lotteryid
     * @param shopid
     * @param activecode
     * @return
     */
    boolean insertTurntable(@Param("wxuserid") String wxuserid, @Param("lotteryid") String lotteryid, @Param("shopid") String shopid, @Param("activecode") String activecode);

    /**
     * 根据活动id查询活动记录
     *
     * @param lotteryid
     * @return
     */
    List<WxuserLotteryDTO> queryAllByLotteryId(@Param("lotteryid") String lotteryid);
}