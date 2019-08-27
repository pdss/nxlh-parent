package com.nxlh.manager.model.to;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserTO {
    private BigDecimal Sumpay;
    private BigDecimal Exp;
    private BigDecimal Score;
    private String UnionId;
    private Integer VipType;
}
