package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbCoupons;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@ToString
@Data
public class CouponsDTO extends TbCoupons {

    /**
     * 能否获取当前券
     */
    private int canget;

    ShopDTO shopinfo;

    String shopname;

    List<TagDTO> tagList;

    Integer vipid;

    Integer count;

}
