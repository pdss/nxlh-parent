package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbCategory;
import com.nxlh.manager.model.dto.CategoryDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbCategoryMapper extends Mapper<TbCategory> {

//    List<TbCategory> queryAllOrderByIsTop();

    /**
     * 重置商品分类表中全部置顶项
     *
     * @return
     */
    int updateTop();

    /**
     * 根据id置顶
     *
     * @param id
     * @return
     */
    int updateTopById(String id);

    /**
     * 分页查询 关联父集
     *
     * @return
     */
    List<CategoryDTO> queryAllAndParent();

    /**
     * 根据父集查找子集
     *
     * @param list
     * @return
     */
    List<CategoryDTO> querySonByParent(List<String> list);

    /**
     * 根据id集合修改isdelete字段
     *
     * @param list
     * @return
     */
    int delByPrimaryKey(List<String> list);

    /**
     * 根据商品id获取所有商品分类，标记已关联的分类
     *
     * @param shopid
     * @return
     */
    List<CategoryDTO> queryAllCategory(@Param("shopid") String shopid);

}