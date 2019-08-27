package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbIndexcategoryMapper;
import com.nxlh.manager.model.dbo.TbIndexcategory;
import com.nxlh.manager.model.dto.IndexcategoryDTO;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import tk.mybatis.mapper.entity.Example;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@CacheConfig(cacheNames = "indexcategory", keyGenerator = "keyGenerator")
public interface IndexcategoryService extends BaseService<IndexcategoryDTO, TbIndexcategoryMapper, TbIndexcategory> {


    @Override
    @Cacheable
    List<IndexcategoryDTO> list(Map<String, Object> parameters, String[] orderby, Integer isAsc);

    @Override
    @CacheEvict(allEntries = true)
    boolean add(IndexcategoryDTO var1);

    @Override
    @CacheEvict(allEntries = true)
    boolean addOrUpdate(IndexcategoryDTO var1);

    @CacheEvict(allEntries = true)
    @Override
    boolean delete(Map<String, Object> parameters);


    @CacheEvict(allEntries = true)
    @Override
    boolean deleteById(String var1);

    @CacheEvict(allEntries = true)
    @Override
    boolean updateById(IndexcategoryDTO var1);

}
