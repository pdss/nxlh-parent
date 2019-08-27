package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_banner")
public class TbBanner extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 标题
     */
    private String title;

    /**
     * 访问的页面地址
     */
    private String url;

    /**
     * 状态： 0禁用1启用
     */
    private Integer status;

    /**
     * 类型 0默认 1弹窗
     */
    private  Integer type;

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
     * 获取图片地址
     *
     * @return image - 图片地址
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置图片地址
     *
     * @param image 图片地址
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    /**
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取访问的页面地址
     *
     * @return url - 访问的页面地址
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置访问的页面地址
     *
     * @param url 访问的页面地址
     */
    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    /**
     * 获取状态： 0禁用1启用
     *
     * @return status - 状态： 0禁用1启用
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态： 0禁用1启用
     *
     * @param status 状态： 0禁用1启用
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}