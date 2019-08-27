package com.nxlh.manager.model;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class UpdateUserScoreVO extends BaseVO {

    /**
     * 小程序积分
     */
    private BigDecimal addvscore;

    /**
     * 其他平台积分
     */
    private BigDecimal addotherscore;


    /**
     * 微信用户
     */
    private String userId;
}
