package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbShop;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.solr.core.mapping.SolrDocument;

@ToString
@Data
@SolrDocument
public class ShopDTO extends TbShop   {
    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 特价、预售、秒杀
     */
    private VipShopDTO vipShop;


    /**
     * 活动类型
     */
    private Integer seckilltype;


}
