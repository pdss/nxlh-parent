package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbMenu;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class MenuDTO extends TbMenu implements BaseDTO {

    /**
     * 菜单是否包含子集的类型
     */
    private String type;


    /**
     * 子集集合
     */
    private List<MenuDTO> children;
}
