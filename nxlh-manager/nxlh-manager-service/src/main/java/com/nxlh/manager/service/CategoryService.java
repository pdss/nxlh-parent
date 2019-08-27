package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbCategoryMapper;
import com.nxlh.manager.model.dbo.TbCategory;
import com.nxlh.manager.model.dto.CategoryDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

import java.util.List;

@CacheConfig(cacheNames = "category", keyGenerator = "keyGenerator")
public interface CategoryService extends BaseService<CategoryDTO, TbCategoryMapper, TbCategory> {

    /**
     * 更新排序 暂未使用
     *
     * @param id
     * @param istop
     * @return
     */
    @CacheEvict(allEntries = true)
    Boolean updateTop(String id, Integer istop);

    //根据商品类型名称查找
    List<CategoryDTO> getByName(String name);

    /**
     * 获取所有分类
     *
     * @return
     */
    @Cacheable
    List<CategoryDTO> getAll();

    /**
     * 删除分类，并遍历删除它的子集
     *
     * @param id
     * @return
     */
    @CacheEvict(allEntries = true)
    boolean delByid(String id);


    /**
     * 遍历所有父集，并插入子集
     *
     * @return
     */
    @Cacheable
    List<CategoryDTO> ergodic();


    /**
     * 根据商品id获取所有分类，并标记关联分类
     *
     * @param shopid
     * @return
     */
    List<CategoryDTO> queryAllCategoryByShopId(String shopid);
}
