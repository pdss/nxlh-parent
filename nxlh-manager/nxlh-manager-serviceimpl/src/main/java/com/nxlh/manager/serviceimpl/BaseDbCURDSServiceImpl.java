package com.nxlh.manager.serviceimpl;


import com.github.binarywang.java.emoji.EmojiConverter;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.*;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.mapper.enums.SqlMethod;
import com.nxlh.manager.model.dbo.BaseDBO;
import com.nxlh.manager.model.dto.BaseDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.extend.ScoreProductMinDTO;
import com.nxlh.manager.utils.TransactionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.solr.client.solrj.SolrServerException;
import org.dozer.Mapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.transaction.annotation.Transactional;
import com.nxlh.manager.service.BaseService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;


/**
 * 这里需要注意：BaseDbCURDSServiceImpl 泛型参数需要和ServiceImpl的泛型参数的顺序一致
 *
 * @param <MAPPER>
 * @param <DBO>
 * @param <DTO>
 */
@Slf4j
public class BaseDbCURDSServiceImpl<MAPPER extends tk.mybatis.mapper.common.Mapper<DBO>, DBO extends BaseDBO, DTO extends BaseDTO> extends BaseRedisCURDServiceImpl implements BaseService<DTO, MAPPER, DBO> {

    @Value("${nxlh.salt}")
    protected String salt;

    @Autowired
    protected MAPPER baseMapper;

    @Autowired
    protected Mapper beanMapper;


    @Autowired
    @Lazy
    protected TransactionUtils transactionUtils;

    @Resource
    @Lazy
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    @Lazy
    protected RabbitTemplate rabbitTemplate;

    //Emoji转换器
    protected EmojiConverter emojiConverter = EmojiConverter.getInstance();


    protected <T> T beanMap(Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        return this.beanMapper.map(source, targetClazz);
    }

    protected <T> List<T> beanListMap(List object, Class<T> targetClazz) {
        if (CollectionUtils.isEmpty(object)) {
            return null;
        }
        var beans = new ArrayList<T>();
        object.forEach(e -> {
            beans.add(this.beanMapper.map(e, targetClazz));
        });
        return beans;
    }

    /**
     * 把PageInfo转成Pagination模型
     *
     * @param source
     * @param targetClazz
     * @param <T>
     * @return
     */
    protected <T> Pagination<T> mapPageInfo(PageInfo source, Class<T> targetClazz) {
        Pagination pageInfo = this.beanMap(source, Pagination.class);
        pageInfo.setList(this.beanListMap(source.getList(), targetClazz));
        return pageInfo;
    }


    protected Example.Builder sqlBuilder() {
        return Example.builder(this.currentDBOClass());
    }

    protected Example.Builder sqlBuilderWithLock() {
        return Example.builder(this.currentDBOClass()).forUpdate();
    }


    protected Class<DBO> currentDBOClass() {
        return ReflectionKit.getSuperClassGenericType(this.getClass(), 1);
    }

    protected Class<MAPPER> currentTableMapperClass() {
        return ReflectionKit.getSuperClassGenericType(this.getClass(), 0);
    }


    protected Class<DTO> currentDTOClass() {
        return ReflectionKit.getSuperClassGenericType(this.getClass(), 2);
    }

    protected String sqlStatement(SqlMethod method) {
        return this.currentTableMapperClass().getTypeName() + "." + method;
    }

    /**
     * 支持批量执行的sqlSession
     *
     * @return
     */
    protected SqlSession sqlSessionBatch() {
        return this.sqlSessionFactory.openSession(ExecutorType.BATCH);
    }


    public static boolean retBool(Integer result) {
//        return null != result && result >= 1;
        return true;
    }


    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean add(DTO entity) {
        if (entity.getId() == null || StringUtils.isEmpty(entity.getId().toString())) {
            entity.setId(IDUtils.genUUID());
            entity.setIsdelete(0);
            entity.setAddtime(new Date());
        }
        var dbo = this.beanMap(entity, this.currentDBOClass());
        return retBool(this.baseMapper.insert(dbo));
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean addBatch(Collection<DTO> entityList) {
        entityList.forEach(e -> {
            this.add(e);
        });
        return true;
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean addOrUpdateBatch(Collection<DTO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        } else {
            int i = 0;
            for (Iterator m = entityList.iterator(); m.hasNext(); ++i) {
                DTO dto = (DTO) m.next();
                if (dto.getId() == null || StringUtils.isEmpty(dto.getId().toString())) {
                    dto.setId(IDUtils.genUUID());
                    dto.setAddtime(new Date());
                    dto.setIsdelete(0);
                }
                if (StringUtils.isEmpty(dto.getId())) {
                    this.add(dto);
                } else {

                    this.updateById(dto);
                }

            }
        }
        return true;
    }

    @Override
    public boolean remove(Example example) {

        try {
            return retBool(this.baseMapper.deleteByExample(example));

        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("删除失败");
            return false;
        }
    }

    @Override
    public boolean removeById(String id) {
        try {
            return retBool(this.baseMapper.deleteByPrimaryKey(id));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("删除失败");
            return false;
        }
    }

    @Override
    public boolean remove(Map<String, Object> parameters) {
        try {
            var example = this.columnsMapToSqlBuilder(parameters);
            var count = this.baseMapper.deleteByExample(example.build());
            return retBool(count);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("删除失败");
            return false;
        }
    }

    @Override
    public boolean removeByIds(Collection<? extends String> ids) {
        var example = Example.builder(this.currentDBOClass()).where(Sqls.custom().andIn("id", ids));
        var count = this.baseMapper.deleteByExample(example);
        return retBool(count);
    }

    @Override
    public boolean deleteById(String id) {
        try {
            var model = this.currentDBOClass().getConstructor().newInstance();
            model.setId(id);
            model.setIsdelete(1);
            return retBool(this.baseMapper.updateByPrimaryKeySelective(model));
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("删除失败");
            return false;
        }
    }

    @Override
    public boolean delete(Map<String, Object> columnMap) {
        try {
            var example = this.columnsMapToSqlBuilder(columnMap);
            var model = this.currentDBOClass().getConstructor().newInstance();
            model.setIsdelete(1);
            var count = this.baseMapper.updateByExampleSelective(model, example.build());
            return retBool(count);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("删除失败");
            return false;
        }
    }


    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean deleteByIds(Collection<? extends String> entityList) {
        entityList.forEach(e -> {
            this.deleteById(e);
        });
        return true;
    }

    @Override
    public boolean delete(Example example) {
        this.baseMapper.deleteByExample(example);
        return true;
    }

    @Override
    public boolean updateById(DTO entity) {
        var dbo = this.beanMap(entity, this.currentDBOClass());
        return retBool(this.baseMapper.updateByPrimaryKeySelective(dbo));
    }

    @Override
    public boolean update(DTO entity, Map<String, Object> columnMap) {
        var example = this.columnsMapToSqlBuilder(columnMap);
        var dbo = this.beanMap(entity, this.currentDBOClass());
        return retBool(this.baseMapper.updateByExample(dbo, example.build()));
    }

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public boolean updateBatchById(Collection<DTO> entityList) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        } else {
            entityList.forEach(e -> {
                this.updateById(e);
            });
        }
        return true;
    }

    @Override
    public boolean addOrUpdate(DTO entity) {
        if (null == entity) {
            return false;
        } else {
            if (StringUtils.isEmpty(entity.getId())) {
                return this.add(entity);
            } else {
                return this.updateById(entity) || this.add(entity);
            }
        }
    }

    @Override
    public DTO getById(String id) {
        var dbo = this.baseMapper.selectByPrimaryKey(id.toString());
        if(dbo==null){
            return null;
        }
        return this.beanMapper.map(dbo, this.currentDTOClass());
    }

    @Override
    public List<DTO> listByIds(Collection<? extends String> ids) {
        if(CollectionUtils.isEmpty(ids)){
            return null;
        }
        Example example = Example
                .builder(this.currentDBOClass())
                .where(Sqls.custom().andIn("id", ids).andEqualTo("isdelete", 0))
                .build();
        example.setOrderByClause("FIELD(`id`,'"+String.join("','",ids)+"')");
        var dbos = this.baseMapper.selectByExample(example);
        return this.beanListMap(dbos, this.currentDTOClass());
    }


    @Override
    public DTO getOne(Map<String, Object> parameters) {
        var example = this.columnsMapToSqlBuilder(parameters);
        return this.getOne(example.build());
    }


    @Override
    public int count(Map<String, Object> parameters) {
        var example = this.columnsMapToSqlBuilder(parameters);
        return this.baseMapper.selectCountByExample(example);
    }

    @Override
    public int count(Example example) {
        if(example!=null){
            example.and(new Example(this.currentDBOClass()).createCriteria().andEqualTo("isdelete", 0));
        }
        return this.baseMapper.selectCountByExample(example);
    }

    @Override
    public List<DTO> list(Example example) {
        if(example!=null){
            example.and(new Example(this.currentDBOClass()).createCriteria().andEqualTo("isdelete", 0));
        }
        var dbos = this.baseMapper.selectByExample(example);
        return this.beanListMap(dbos, this.currentDTOClass());
    }

    @Override
    public DTO getOne(Example example) {
        if(example!=null){
            example.and(new Example(this.currentDBOClass()).createCriteria().andEqualTo("isdelete", 0));
        }
        var dbos = this.baseMapper.selectOneByExample(example);
        return this.beanMap(dbos, this.currentDTOClass());
    }

    @Override
    public Pagination<DTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        example.and(new Example(this.currentDBOClass()).createCriteria().andEqualTo("isdelete", 0));
        var list = this.baseMapper.selectByExample(example);
        PageInfo<DBO> pageInfo = new PageInfo<DBO>(list);

        return mapPageInfo(pageInfo, this.currentDTOClass());
    }

    @Override
    public Pagination<DTO> query(PageParameter page, Map<String, Object> filters, String[] sorts, Integer[] isAsc) throws IOException, SolrServerException {
        throw new NotImplementedException("撸起来");
    }

    @Override
    public List<DTO> query(Map<String, Object> filters, String[] sorts, Integer[] isAsc) {
        throw new NotImplementedException("撸起来");
    }

    protected Example.Builder columnsMapToSqlBuilder(Map<String, Object> columnsMap) {
        var example = Example.builder(this.currentDBOClass()).where(Sqls.custom().andEqualTo("isdelete", 0));
        if (columnsMap != null && columnsMap.size() > 0) {

            for (var key : columnsMap.keySet()) {
                var val = columnsMap.get(key);
                example = example.andWhere(Sqls.custom().andEqualTo(key, val));
            }
        }
        return example;
    }


    /**
     * 重写映射，将and改写成or
     *
     * @param columnsMap
     * @return
     */
    protected Example.Builder columnsMapToSqlBuilderToOR(Map<String, Object> columnsMap) {
        var example = Example.builder(this.currentDBOClass()).where(Sqls.custom().andEqualTo("isdelete", 0));
        if (columnsMap != null && columnsMap.size() > 0) {

            for (var key : columnsMap.keySet()) {
                var val = columnsMap.get(key);
                example = example.orWhere(Sqls.custom().orEqualTo(key, val));
            }
        }
        return example;
    }

    @Override
    public List<DTO> list(Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = this.columnsMapToSqlBuilder(parameters);

        if (ArrayUtils.isNotEmpty(orderby)) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return this.list(example.build());
    }

    @Override
    public Pagination<DTO> page(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = this.columnsMapToSqlBuilder(parameters);
        if (ArrayUtils.isNotEmpty(orderby)) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return page(page, example.build());
    }


}

