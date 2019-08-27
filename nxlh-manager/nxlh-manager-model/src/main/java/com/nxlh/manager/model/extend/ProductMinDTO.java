package com.nxlh.manager.model.extend;

import com.nxlh.manager.model.dto.VipShopDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ProductMinDTO implements Serializable {
    private String shopname;
    private String thumbnails;
    private String id;
    private BigDecimal saleprice;
    private BigDecimal discount;
    private Integer isprebuy;
    private BigDecimal prebuyprice;

    //=============扩展字段========

    //购买数量
    private Integer count;



}
