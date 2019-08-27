package com.nxlh.manager.mapper;


import com.nxlh.manager.model.dbo.TbLotteryItem;
import com.nxlh.manager.model.dto.LotteryItemDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbLotteryItemMapper extends Mapper<TbLotteryItem> {

    boolean insetLotteryItemList(List<LotteryItemDTO> list);
}