package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbCategoryMapper;
import com.nxlh.manager.model.dbo.TbCategory;
import com.nxlh.manager.model.dto.CategoryDTO;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.nxlh.manager.service.CategoryService;
import tk.mybatis.mapper.util.Sqls;

@Service(interfaceClass = CategoryService.class)
public class CategoryServiceImp extends BaseDbCURDSServiceImpl<TbCategoryMapper, TbCategory, CategoryDTO> implements CategoryService {

    @Override
    @Transactional(
            rollbackFor = {Exception.class}
    )
    public Boolean updateTop(String id, Integer istop) {
        try {
            int i = baseMapper.updateTop();
            //申请取消置顶
            if (istop == 3) {
                return true;
            }
            //申请置顶
            int j = baseMapper.updateTopById(id);
            return j > 0 ? true : false;

        } catch (Exception ex) {
            throw ex;
        }
    }

    //根据商品类型名称查找
    @Override
    public List<CategoryDTO> getByName(String name) {
        if (!("".equals(name.trim())) && name != null) {
            Example example = new Example(TbCategory.class);
            example.createCriteria().andLike("categoryname", "%" + name + "%").andEqualTo("isdelete", 0);
            PageHelper.startPage(0, 5);
            List<TbCategory> tbCategories = baseMapper.selectByExample(example);
            return this.beanListMap(tbCategories, this.currentDTOClass());
        }
        return null;
    }

    @Override
    public List<CategoryDTO> getAll() {
        Example example = Example.builder(TbCategory.class).where(Sqls.custom().andEqualTo("isdelete", 0)).orderBy("sort").build();
        List<TbCategory> tbCategories = this.baseMapper.selectByExample(example);
        return this.beanListMap(tbCategories, this.currentDTOClass());
    }

    @Override
    public boolean delByid(String id) {
        List<String> ids = new ArrayList<>();
        ids.add(id);
        List<String> delIds = new ArrayList<>();
        delIds.add(id);
        boolean isErgodic = true;
        while (isErgodic) {
            List<CategoryDTO> categoryDTOList = ergodicSon(ids);
            if (categoryDTOList != null && categoryDTOList.size() > 0) {
                List<String> sonIds = categoryDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
                if (sonIds != null && sonIds.size() > 0) {
                    ids = sonIds;
                    delIds.addAll(sonIds);
                } else {
                    isErgodic = false;
                }
            } else {
                isErgodic = false;
            }
        }
        boolean result = this.transactionUtils.transact((a) -> {
            this.baseMapper.delByPrimaryKey(delIds);
        });
        return result;
    }

    @Override
    public List<CategoryDTO> ergodic() {
        //获取所有根级分类
        Example example = Example.builder(TbCategory.class).where(Sqls.custom()
                .andEqualTo("parentid", "0")
                .andEqualTo("isdelete", 0)).orderBy("sort").build();
        List<TbCategory> tbCategories = this.baseMapper.selectByExample(example);
        List<CategoryDTO> categoryDTOList = this.beanListMap(tbCategories, this.currentDTOClass());
        return ergodicSub(categoryDTOList);
    }

    @Override
    public List<CategoryDTO> queryAllCategoryByShopId(String shopid) {
        if (StringUtils.isEmpty(shopid)) shopid = "";
        return this.baseMapper.queryAllCategory(shopid);
    }

    /**
     * 根据父集查找子集
     *
     * @param ids
     * @return
     */
    private List<CategoryDTO> ergodicSon(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            List<CategoryDTO> categoryDTOList = this.baseMapper.querySonByParent(ids);
            List<String> sonIds = categoryDTOList.stream().map(e -> e.getId()).collect(Collectors.toList());
            if (categoryDTOList.size() > 0) return categoryDTOList;
        }
        return null;
    }

    /**
     * 根据父集遍历所有子集
     *
     * @param categoryList
     * @return
     */
    private List<CategoryDTO> ergodicSub(List<CategoryDTO> categoryList) {
        if (categoryList != null && categoryList.size() > 0) {
            List<String> ids = new ArrayList<>();
            for (CategoryDTO categoryDTO : categoryList) {
                Example example = Example.builder(TbCategory.class).where(Sqls.custom()
                        .andEqualTo("parentid", categoryDTO.getId())
                        .andEqualTo("isdelete", 0)).orderBy("sort").build();
                List<TbCategory> tbCategories = this.baseMapper.selectByExample(example);
                List<CategoryDTO> categorySubList = this.beanListMap(tbCategories, this.currentDTOClass());
                if (categorySubList != null && categorySubList.size() > 0) ergodicSub(categorySubList);
                categoryDTO.setSubCategorys(categorySubList);
            }
            return categoryList;
        }
        return null;
    }


    //重写分页
    @Override
    public Pagination<CategoryDTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<CategoryDTO> categoryDTOList = this.baseMapper.queryAllAndParent();
        PageInfo<CategoryDTO> pageInfo = new PageInfo<CategoryDTO>(categoryDTOList);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addOrUpdate(CategoryDTO entity) {
        return super.addOrUpdate(entity);
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteById(String id) {
        return super.deleteById(id);
    }
}
