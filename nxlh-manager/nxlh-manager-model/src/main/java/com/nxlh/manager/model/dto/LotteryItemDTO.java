package com.nxlh.manager.model.dto;


import com.nxlh.manager.model.dbo.TbLotteryItem;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class LotteryItemDTO extends TbLotteryItem implements BaseDTO {

    //兑奖码
//    String activecode;

    /**
     * 被抽中的数量
     */
//    Integer getcount=0;



}
