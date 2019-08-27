package com.nxlh.minapp.model;

import com.nxlh.manager.model.extend.ProductMinDTO;
import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ToString
public class ProductByCategoryVO extends BaseVO {
    /**
     * 分类id,categoryid
     */
    @NotBlank
    private String cid;

    /**
     * 分类下的商品
     */
    private List<ProductMinDTO> products;
}
