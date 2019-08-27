package com.nxlh.minapp.model;

import com.nxlh.manager.model.enums.AccoutEnums;
import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;

/**
 * 二手回收寄送
 */
@Data
public class RecycleSendVO extends BaseVO {

    /**
     * 用户的工单号
     */
    private String orderid;

    /**
     * 快递
     */
     private String express;


    /**
     * 运单号
     */
    private String expressno;

    private String account;

    private AccoutEnums.AccoutTypeEnums accounttype;
}
