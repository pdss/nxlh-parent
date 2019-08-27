package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbWxuserLottery;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class WxuserLotteryDTO extends TbWxuserLottery {

    private WxUserDTO wxuser;

    private ShopDTO shop;


    /**
     * 关联抽奖项
     */
    private LotteryItemDTO lotteryItem;

    LotteryDTO lottery;


}
