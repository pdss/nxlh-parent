package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbWxuserOtherRecordMapper;
import com.nxlh.manager.model.dbo.TbWxuserOtherRecord;
import com.nxlh.manager.model.dto.WxUserOtherRecordDTO;
import com.nxlh.manager.service.WxUserOtherRecordService;

@Service(interfaceClass = WxUserOtherRecordService.class)
public class WxUserScoreRecordServiceImp extends BaseDbCURDSServiceImpl<TbWxuserOtherRecordMapper, TbWxuserOtherRecord, WxUserOtherRecordDTO> implements WxUserOtherRecordService {


}
