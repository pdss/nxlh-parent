package com.nxlh.manager.model.extend;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@ToString
public class ScoreProductMinDTO implements Serializable {
    private String shopname;
    private String thumbnails;
    private String id;
    //    private Integer score;
    private BigDecimal discount;
    private BigDecimal saleprice;


}
