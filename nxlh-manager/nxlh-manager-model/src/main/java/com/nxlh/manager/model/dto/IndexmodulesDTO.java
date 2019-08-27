package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbIndexmodules;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@ToString
@Data
public class IndexmodulesDTO extends TbIndexmodules {

    private List<ModulesProductsDTO> products;

}
