package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_indexcategory")
public class TbIndexcategory extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 标题文字
     */
    private String title;

    /**
     * 背景图
     */
    private String image;

    /**
     * 传参
     */
    private String params;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return addtime
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * @param addtime
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * @return isdelete
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * @param isdelete
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 获取标题文字
     *
     * @return title - 标题文字
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题文字
     *
     * @param title 标题文字
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取背景图
     *
     * @return image - 背景图
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置背景图
     *
     * @param image 背景图
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取传参
     *
     * @return params - 传参
     */
    public String getParams() {
        return params;
    }

    /**
     * 设置传参
     *
     * @param params 传参
     */
    public void setParams(String params) {
        this.params = params == null ? null : params.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }
}