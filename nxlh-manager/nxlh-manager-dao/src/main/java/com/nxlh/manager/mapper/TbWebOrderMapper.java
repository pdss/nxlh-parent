package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbWebOrder;
import com.nxlh.manager.model.dto.WebOrderDTO;
import tk.mybatis.mapper.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface TbWebOrderMapper extends Mapper<TbWebOrder> {


    Boolean refundByIds(List<String> list);

    List<WebOrderDTO> getOverOrderByDate(@Param("dateTime") Date start);

    Boolean CloseByIds(List<Long> list);

    List<WebOrderDTO> getOverOrderByIds(List<Long> list);
}