package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbOrder;
import com.nxlh.manager.model.extend.ProductMinDTO;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@ToString
@Data
public class OrderDTO extends TbOrder {

    // private WxUserDTO wxuser;

    /**
     * 用户的收货地址Id
     */
    private String receiveAddressId;

    //库表关联一个商品
    private ShopDTO product;

    /**
     * 预生成订单时，关联多个商品
     */
    private List<ProductMinDTO> previewProducts;

    /**
     * 最多可抵扣的积分数
     */
    private BigDecimal maxScore;

    /**
     * 退款待审核数量
     */
    private Integer refundcount;

    /**
     * 能否使用优惠券
     * 主要是针对 1元补差
     */
    private Integer cancoupon;


    /**
     * 秒杀活动id
     */
    private String seckillId;


    /**
     * 逾期天数
     */
    private Integer overdue;

    /**
     * 逾期金额
     */
    private BigDecimal overdueMoney;

    //是否免押金
    private boolean freecost =false;

    /**
     * 到期日
     */
    private Date enddate;

    /**
     * 租金
     */
    private BigDecimal rentprice;

}
