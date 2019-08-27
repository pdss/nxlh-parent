package com.nxlh.manager.model.extend;

import com.nxlh.manager.model.dto.VipShopDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
public class VipProductMinDTO extends ProductMinDTO{
    public VipShopDTO vipShop;
}