package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbRecycleProductMapper;
import com.nxlh.manager.model.dbo.TbRecycleProduct;
import com.nxlh.manager.model.dto.RecycleProductDTO;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import org.springframework.cache.annotation.CacheEvict;


public interface RecycleProductService extends BaseService<RecycleProductDTO, TbRecycleProductMapper, TbRecycleProduct> {

    /**
     * 创建工单
     *
     * @param model
     * @return 工单号
     */
    WxUserRecycleDTO order(WxUserRecycleDTO model);


    /**
     * 添加或编辑回收商品信息
     *
     * @param entity
     * @return
     */
//    @CacheEvict(allEntries = true)
    boolean addOrUpdateRecycleProduct(RecycleProductDTO entity);


    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
//    @CacheEvict(allEntries = true)
    boolean recycleProductDeleteById(String id);




}
