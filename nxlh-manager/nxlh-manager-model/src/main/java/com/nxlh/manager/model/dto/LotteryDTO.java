package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbLottery;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@ToString
@Data
public class LotteryDTO extends TbLottery implements BaseDTO {

    /**
     * 关联的奖品
     */
    List<LotteryItemDTO> lotteryItemList;

    //抽中的商品
    LotteryItemDTO award;

    //参加活动的人员总数
    Integer personscount;

    //抽奖次数
    Integer countNum;


    /**
     * 参与用户的抽奖记录
     */
    List<WxuserLotteryDTO> userLotteries;

    //手动指定的参与用户id
    List<String> joinuserids;

    /**
     * 参与用户的信息
     * 仅：Nickname ,id,phone
     */
    List<Map<String,Object>> joinusers;

    /**
     * 奖品抽光，结束
     */
    Boolean isover = false;

    /**
     * 当前用户是否有资格参数
     */
    Boolean hasTicket = false;

}
