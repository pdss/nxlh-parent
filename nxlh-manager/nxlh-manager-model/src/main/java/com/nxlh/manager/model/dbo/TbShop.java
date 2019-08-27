package com.nxlh.manager.model.dbo;


import lombok.ToString;
import org.apache.solr.client.solrj.beans.Field;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.solr.core.mapping.SolrDocument;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Table(name = "tb_shop")
@SolrDocument(collection = "ik_core")
@ToString
public class TbShop extends BaseDBO {
    public TbShop() {
        this.discount = 1F;
        this.limitbuy = 0;
        this.istop = 0;
        this.prebuyprice = BigDecimal.ZERO;
//        this.score = BigDecimal.ZERO;
        this.isnew = 0;
        this.cancoupon = 1;
        this.dailyrent = BigDecimal.ZERO;
        this.monthlyrent = BigDecimal.ZERO;
        this.sort=1;
    }

    /**
     * 商品名称
     */
    @Length(max = 80)
    @Field()
    private String shopname;

    /**
     * 商品描述
     */
    private String description;

    /**
     * 打折
     */
    private Float discount;

    /**
     * 库存数
     */
    private Integer stockcount;

    /**
     * 促销价
     */
    private BigDecimal saleprice;

    /**
     * 是否出租
     */
    @Field
    private Integer isrent;

    /**
     * 限购数，0不限购
     */
    private Integer limitbuy;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 是否置顶
     */
    private Integer istop;

    /**
     * 是否热门(推荐)
     */
    private Integer ishot;

    /**
     * 0下架 1上架
     */
    @Field
    private Integer status;

    /**
     * 是否预售
     */
    private Integer isprebuy;

    /**
     * 预售价
     */
    private BigDecimal prebuyprice;

    /**
     * 图片列表
     */
    private String images;

    /**
     * 首图
     */
    private String thumbnails;

    /**
     * 是否积分商品
     */
    @Field
    private Integer isscore;

    /**
     * 是否新品
     */
    private Integer isnew;

    /**
     * 所需积分
     */
//    private BigDecimal score;


    /**
     * 商品分类
     */
    private TbCategory category;

    /**
     * 游戏分类
     */
    private TbTag tag;

    /**
     * 商品分类
     */
    private List<TbCategory> categoryList;

    /**
     * 游戏分类
     */
    private List<TbTag> tagList;

    /**
     * 关联商品分类
     */
    private List<TbGameCategory> gameCategoryList;

    /**
     * 关联游戏分类
     */
    private List<TbGameTag> gameTagList;

    /**
     * 是否售卖
     */
    @Field
    private Integer issale;

    /**
     * 能否使用优惠券
     */
    private Integer cancoupon;

    /**
     * 简称
     */
    private String shortname;

    /**
     * 类型
     */
    @Field
    private Integer genres;

    /**
     * 上市日期
     */
    private String selltime;

    /**
     * 日租金
     */
    private BigDecimal dailyrent;

    /**
     * 月租金
     */
    private BigDecimal monthlyrent;


    /***
     * 排序
     */
    @Field
    private Integer sort;


    public BigDecimal getDailyrent() {
        return dailyrent;
    }

    public void setDailyrent(BigDecimal dailyrent) {
        this.dailyrent = dailyrent;
    }

    public BigDecimal getMonthlyrent() {
        return monthlyrent;
    }

    public void setMonthlyrent(BigDecimal monthlyrent) {
        this.monthlyrent = monthlyrent;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    public Integer getGenres() {
        return genres;
    }

    public void setGenres(Integer genres) {
        this.genres = genres;
    }

    public String getSelltime() {
        return selltime;
    }

    public void setSelltime(String selltime) {
        this.selltime = selltime;
    }

    public List<TbGameCategory> getGameCategoryList() {
        return gameCategoryList;
    }

    public void setGameCategoryList(List<TbGameCategory> gameCategoryList) {
        this.gameCategoryList = gameCategoryList;
    }

    public List<TbGameTag> getGameTagList() {
        return gameTagList;
    }

    public void setGameTagList(List<TbGameTag> gameTagList) {
        this.gameTagList = gameTagList;
    }

    public List<TbCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<TbCategory> categoryList) {
        this.categoryList = categoryList;
    }

    public List<TbTag> getTagList() {
        return tagList;
    }

    public void setTagList(List<TbTag> tagList) {
        this.tagList = tagList;
    }

    public TbCategory getCategory() {
        return category;
    }

    public void setCategory(TbCategory category) {
        this.category = category;
    }

    public TbTag getTag() {
        return tag;
    }

    public void setTag(TbTag tag) {
        this.tag = tag;
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
     * 获取商品名称
     *
     * @return shopname - 商品名称
     */
    public String getShopname() {
        return shopname;
    }

    /**
     * 设置商品名称
     *
     * @param shopname 商品名称
     */
    public void setShopname(String shopname) {
        this.shopname = shopname == null ? null : shopname.trim();
    }

    /**
     * 获取商品描述
     *
     * @return description - 商品描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置商品描述
     *
     * @param description 商品描述
     */
    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    /**
     * 获取打折
     *
     * @return discount - 打折
     */
    public Float getDiscount() {
        return discount;
    }

    /**
     * 设置打折
     *
     * @param discount 打折
     */
    public void setDiscount(Float discount) {
        this.discount = discount;
    }

    /**
     * 获取库存数
     *
     * @return stockcount - 库存数
     */
    public Integer getStockcount() {
        return stockcount;
    }

    /**
     * 设置库存数
     *
     * @param stockcount 库存数
     */
    public void setStockcount(Integer stockcount) {
        this.stockcount = stockcount;
    }

    /**
     * 获取促销价
     *
     * @return saleprice - 促销价
     */
    public BigDecimal getSaleprice() {
        return saleprice;
    }

    /**
     * 设置促销价
     *
     * @param saleprice 促销价
     */
    public void setSaleprice(BigDecimal saleprice) {
        this.saleprice = saleprice;
    }

    /**
     * 获取是否出租
     *
     * @return isrent - 是否出租
     */
    public Integer getIsrent() {
        return isrent;
    }

    /**
     * 设置是否出租
     *
     * @param isrent 是否出租
     */
    public void setIsrent(Integer isrent) {
        this.isrent = isrent;
    }

    /**
     * 获取限购数，0不限购
     *
     * @return limitbuy - 限购数，0不限购
     */
    public Integer getLimitbuy() {
        return limitbuy;
    }

    /**
     * 设置限购数，0不限购
     *
     * @param limitbuy 限购数，0不限购
     */
    public void setLimitbuy(Integer limitbuy) {
        this.limitbuy = limitbuy;
    }

    /**
     * 获取备注信息
     *
     * @return remark - 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取是否置顶
     *
     * @return istop - 是否置顶
     */
    public Integer getIstop() {
        return istop;
    }

    /**
     * 设置是否置顶
     *
     * @param istop 是否置顶
     */
    public void setIstop(Integer istop) {
        this.istop = istop;
    }

    /**
     * 获取是否热门(推荐)
     *
     * @return ishot - 是否热门(推荐)
     */
    public Integer getIshot() {
        return ishot;
    }

    /**
     * 设置是否热门(推荐)
     *
     * @param ishot 是否热门(推荐)
     */
    public void setIshot(Integer ishot) {
        this.ishot = ishot;
    }

    /**
     * 获取0下架 1上架
     *
     * @return status - 0下架 1上架
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置0下架 1上架
     *
     * @param status 0下架 1上架
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取是否预售
     *
     * @return isprebuy - 是否预售
     */
    public Integer getIsprebuy() {
        return isprebuy;
    }

    /**
     * 设置是否预售
     *
     * @param isprebuy 是否预售
     */
    public void setIsprebuy(Integer isprebuy) {
        this.isprebuy = isprebuy;
    }

    /**
     * 获取预售价
     *
     * @return prebuyprice - 预售价
     */
    public BigDecimal getPrebuyprice() {
        return prebuyprice;
    }

    /**
     * 设置预售价
     *
     * @param prebuyprice 预售价
     */
    public void setPrebuyprice(BigDecimal prebuyprice) {
        this.prebuyprice = prebuyprice;
    }

    /**
     * 获取图片列表
     *
     * @return images - 图片列表
     */
    public String getImages() {
        return images;
    }

    /**
     * 设置图片列表
     *
     * @param images 图片列表
     */
    public void setImages(String images) {
        this.images = images == null ? null : images.trim();
    }

    /**
     * 获取首图
     *
     * @return thumbnails - 首图
     */
    public String getThumbnails() {
        return thumbnails;
    }

    /**
     * 设置首图
     *
     * @param thumbnails 首图
     */
    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails == null ? null : thumbnails.trim();
    }


    public Integer getIsscore() {
        return isscore;
    }

    public void setIsscore(Integer isscore) {
        this.isscore = isscore;
    }

//    public BigDecimal getScore() {
//        return score;
//    }
//
//    public void setScore(BigDecimal score) {
//        this.score = score;
//    }

    public Integer getIsnew() {
        return isnew;
    }

    public void setIsnew(Integer isnew) {
        this.isnew = isnew;
    }

    public Integer getIssale() {
        return issale;
    }

    public void setIssale(Integer issale) {
        this.issale = issale;
    }

    public Integer getCancoupon() {
        return cancoupon;
    }

    public void setCancoupon(Integer cancoupon) {
        this.cancoupon = cancoupon;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

}