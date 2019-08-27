package com.nxlh.manager.model.vo;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class VipConfigJsonModel implements Serializable {
    private String name;
    private Integer level;
    private BigDecimal exp;
    //每天降的经验的等级
    private BigDecimal downgrade;
    //上一个等级的经验
    private BigDecimal lower;
}
