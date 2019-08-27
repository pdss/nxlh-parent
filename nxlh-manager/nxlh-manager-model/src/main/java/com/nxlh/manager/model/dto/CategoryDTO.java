package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbCategory;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class CategoryDTO extends TbCategory {

    /**
     * 父级名称
     */
    private String parentname;


    private List<CategoryDTO> subCategorys;

    /**
     * 是否选中
     */
    private Integer ischeck;
}
