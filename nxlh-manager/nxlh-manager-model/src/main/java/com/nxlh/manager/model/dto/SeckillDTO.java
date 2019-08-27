package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbSeckill;
import com.nxlh.manager.model.extend.ProductMinDTO;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@ToString
@Data
public class SeckillDTO extends TbSeckill  {

    private List<ProductMinDTO> previewPros;


}
