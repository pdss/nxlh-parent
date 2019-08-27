package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_tag")
public class TbTag extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 游戏类型
     */
    private String tagname;

    /**
     * 置顶
     */
    private Integer istop;

    /**
     * logo
     */
    private String logo;

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
     * 获取游戏类型
     *
     * @return tagname - 游戏类型
     */
    public String getTagname() {
        return tagname;
    }

    /**
     * 设置游戏类型
     *
     * @param tagname 游戏类型
     */
    public void setTagname(String tagname) {
        this.tagname = tagname == null ? null : tagname.trim();
    }

    /**
     * 获取置顶
     *
     * @return istop - 置顶
     */
    public Integer getIstop() {
        return istop;
    }

    /**
     * 设置置顶
     *
     * @param istop 置顶
     */
    public void setIstop(Integer istop) {
        this.istop = istop;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}