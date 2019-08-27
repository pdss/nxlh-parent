package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_lottery_item")
public class TbLotteryItem extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 抽奖id
     */
    private String lotteryid;

    /**
     * 数量
     */
    private Integer count;

    /**
     * 概率,小于1
     */
    private BigDecimal percent;

    /**
     * 关联商品id
     */
    private String productid;

    /**
     * 商品名称
     */
    private String productname;

    /**
     * 商品图片
     */
    private String productimage;

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
     * 获取抽奖id
     *
     * @return lotteryid - 抽奖id
     */
    public String getLotteryid() {
        return lotteryid;
    }

    /**
     * 设置抽奖id
     *
     * @param lotteryid 抽奖id
     */
    public void setLotteryid(String lotteryid) {
        this.lotteryid = lotteryid == null ? null : lotteryid.trim();
    }

    /**
     * 获取数量
     *
     * @return count - 数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置数量
     *
     * @param count 数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取概率,小于1
     *
     * @return percent - 概率,小于1
     */
    public BigDecimal getPercent() {
        return percent;
    }

    /**
     * 设置概率,小于1
     *
     * @param percent 概率,小于1
     */
    public void setPercent(BigDecimal percent) {
        this.percent = percent;
    }

    /**
     * 获取关联商品id
     *
     * @return productid - 关联商品id
     */
    public String getProductid() {
        return productid;
    }

    /**
     * 设置关联商品id
     *
     * @param productid 关联商品id
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * 获取商品名称
     *
     * @return productname - 商品名称
     */
    public String getProductname() {
        return productname;
    }

    /**
     * 设置商品名称
     *
     * @param productname 商品名称
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * 获取商品图片
     *
     * @return productimage - 商品图片
     */
    public String getProductimage() {
        return productimage;
    }

    /**
     * 设置商品图片
     *
     * @param productimage 商品图片
     */
    public void setProductimage(String productimage) {
        this.productimage = productimage == null ? null : productimage.trim();
    }
}