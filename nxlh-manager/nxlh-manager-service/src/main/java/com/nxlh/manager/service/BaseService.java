package com.nxlh.manager.service;

import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.model.dbo.BaseDBO;
import com.nxlh.manager.model.dto.BaseDTO;
import org.apache.solr.client.solrj.SolrServerException;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface BaseService<T extends BaseDTO, M extends tk.mybatis.mapper.common.Mapper<V>, V extends BaseDBO> {

    boolean add(T var1);


    boolean addBatch(Collection<T> entityList);

    boolean addOrUpdateBatch(Collection<T> entityList) ;

    boolean remove(Example example);

    boolean removeById(String var1);

    boolean remove(Map<String, Object> parameters);

    boolean removeByIds(Collection<? extends String> var1);

    boolean deleteById(String var1);

    boolean delete(Map<String, Object> parameters);

    boolean deleteByIds(Collection<? extends String> entityList);

    boolean delete(Example example);

    boolean updateById(T var1);

    boolean update(T var1, Map<String, Object> parameters);

    boolean updateBatchById(Collection<T> entityList);

    boolean addOrUpdate(T var1);

    T getById(String var1);

    List<T> listByIds(Collection<? extends String> var1);

    T getOne(Map<String, Object> parameters);

    int count(Map<String, Object> parameters);

    int count(Example example);

    List<T> list(Map<String, Object> parameters, String[] orderby, Integer isAsc);

    Pagination<T> page(PageParameter var1, Map<String, Object> parameters, String[] orderby, Integer isAsc);

    List<T> list(Example example);

    T getOne(Example example);

    Pagination<T> page(PageParameter page, Example example);

    Pagination<T> query(PageParameter page, Map<String, Object> filters, String[] sorts, Integer[] isAsc) throws Exception;

    List<T> query(Map<String, Object> filters, String[] sorts, Integer[] isAsc);


}
