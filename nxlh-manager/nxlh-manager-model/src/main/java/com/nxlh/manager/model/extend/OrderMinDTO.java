package com.nxlh.manager.model.extend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 简略版订单模型
 */
@Data
@ToString
public class OrderMinDTO implements Serializable {
    private String id;
    private String orderno;
    private String productname;
    private int buycount;
    private BigDecimal productprice;
    private BigDecimal payprice;
    private String productimage;
    private int status;
    private int ordertype;

    /*
    是否能退款
     */
    private int canrefund;

    /**
     * 子状态
     */
    private String substatus;

}
