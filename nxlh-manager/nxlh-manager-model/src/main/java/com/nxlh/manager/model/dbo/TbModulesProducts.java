package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_modules_products")
public class TbModulesProducts extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    private String moduleid;

    private String productid;

    private String productname;

    private BigDecimal price;

    private BigDecimal nowprice;

    private String thumbnail;

    private Integer sort;

    /**
     * 排序
     *
     * @return
     */
    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

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
     * @return moduleid
     */
    public String getModuleid() {
        return moduleid;
    }

    /**
     * @param moduleid
     */
    public void setModuleid(String moduleid) {
        this.moduleid = moduleid == null ? null : moduleid.trim();
    }

    /**
     * @return productid
     */
    public String getProductid() {
        return productid;
    }

    /**
     * @param productid
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * @return productname
     */
    public String getProductname() {
        return productname;
    }

    /**
     * @param productname
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * @return price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return nowprice
     */
    public BigDecimal getNowprice() {
        return nowprice;
    }

    /**
     * @param nowprice
     */
    public void setNowprice(BigDecimal nowprice) {
        this.nowprice = nowprice;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}