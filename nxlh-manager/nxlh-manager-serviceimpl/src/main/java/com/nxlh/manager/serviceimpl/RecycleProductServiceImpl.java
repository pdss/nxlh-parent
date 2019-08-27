package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.utils.*;
import com.nxlh.manager.mapper.TbRecycleProductMapper;
import com.nxlh.manager.model.dbo.TbRecycleProduct;
import com.nxlh.manager.model.dto.*;
import com.nxlh.manager.model.enums.RecycleEnums;
import com.nxlh.manager.service.*;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;

import java.util.*;

import static com.nxlh.manager.amqp.queue.ShopQueue.*;

@Service(interfaceClass = RecycleProductService.class)
public class RecycleProductServiceImpl extends BaseDbCURDSServiceImpl<TbRecycleProductMapper, TbRecycleProduct, RecycleProductDTO> implements RecycleProductService {

    @Autowired
    @Lazy
    private WxUserRecycleService wxUserRecycleService;

    @Override
    public WxUserRecycleDTO order(WxUserRecycleDTO model) {

             var shop = this.getById(model.getProductid());
             model.setProductimage(shop.getThumbnails());
             model.setPrice(shop.getPrice());
             model.setAddtime(new Date());
             model.setProducttype(shop.getType());
             model.setStatus(RecycleEnums.Talk.getValue());
             model.setOrderno(GenerateNum.getInstance().GenerateOrder());
             model.setProductname(shop.getShopname());

             this.wxUserRecycleService.add(model);
             return model;


    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean addOrUpdateRecycleProduct(RecycleProductDTO entity) {
        var isEdit = StringUtils.isEmpty(entity.getId());
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(IDUtils.genUUID());
            entity.setIsdelete(0);
            entity.setAddtime(new Date());
//                this.add(entity);
        }

        boolean result = this.transactionUtils.transact((a) -> {
            if (isEdit) {
                this.add(entity);
            } else {
                this.updateById(entity);
            }
            //消息队列，异步写入引擎
            this.rabbitTemplate.convertAndSend(SHOP_SOLR_MASTER_EX, SHOP_SOLR_SAVE_QUEUE, entity, new CorrelationData(IDUtils.genUUID()));

        });

        return result;
    }

    @Override
//    @CacheEvict(allEntries = true)
    public boolean recycleProductDeleteById(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.deleteById(id);
            this.rabbitTemplate.convertAndSend(SHOP_SOLR_MASTER_EX, SHOP_SOLR_DELETE_QUEUE, id, new CorrelationData(IDUtils.genUUID()));
        });
        return result;
    }

}
