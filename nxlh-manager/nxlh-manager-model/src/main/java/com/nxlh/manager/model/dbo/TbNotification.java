package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_notification")
public class TbNotification extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 标题
     */
    private String title;

    /**
     * 二级标题
     */
    private String subtitle;

    /**
     * 跳转页面地址
     */
    private String path;

    /**
     * logo
     */
    private String image;

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
     * 获取二级标题
     *
     * @return subtitle - 二级标题
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置二级标题
     *
     * @param subtitle 二级标题
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    /**
     * 获取跳转页面地址
     *
     * @return path - 跳转页面地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置跳转页面地址
     *
     * @param path 跳转页面地址
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
    }

    /**
     * 获取logo
     *
     * @return image - logo
     */
    public String getImage() {
        return image;
    }

    /**
     * 设置logo
     *
     * @param image logo
     */
    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }
}