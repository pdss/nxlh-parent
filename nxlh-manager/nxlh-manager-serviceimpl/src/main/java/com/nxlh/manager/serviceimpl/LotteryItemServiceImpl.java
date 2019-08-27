package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbLotteryItemMapper;

import com.nxlh.manager.model.dbo.TbLotteryItem;
import com.nxlh.manager.model.dto.LotteryItemDTO;
import com.nxlh.manager.service.LotteryItemService;

@Service(interfaceClass = LotteryItemService.class)
public class LotteryItemServiceImpl extends BaseDbCURDSServiceImpl<TbLotteryItemMapper, TbLotteryItem, LotteryItemDTO> implements LotteryItemService {

}
