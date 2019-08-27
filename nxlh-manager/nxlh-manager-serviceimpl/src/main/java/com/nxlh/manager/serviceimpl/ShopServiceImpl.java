package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbShopMapper;
import com.nxlh.manager.model.dbo.*;
import com.nxlh.manager.model.dto.SeckillDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.extend.ProductMinDTO;
import com.nxlh.manager.model.extend.ScoreProductMinDTO;
import com.nxlh.manager.model.extend.VipProductMinDTO;
import com.nxlh.manager.solr.repository.ShopSolrRepository;
import com.nxlh.manager.service.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.io.IOException;
import java.util.*;

import static com.nxlh.manager.amqp.queue.ShopQueue.SHOP_SOLR_DELETE_QUEUE;
import static com.nxlh.manager.amqp.queue.ShopQueue.SHOP_SOLR_MASTER_EX;
import static com.nxlh.manager.amqp.queue.ShopQueue.SHOP_SOLR_SAVE_QUEUE;

@Service(interfaceClass = ShopService.class)
@Slf4j
public class ShopServiceImpl extends BaseDbCURDSServiceImpl<TbShopMapper, TbShop, ShopDTO> implements ShopService {


    @Autowired
    private GameCategoryService gameCategoryService;

    @Autowired
    private GameTagService gameTagService;

    @Autowired
    @Lazy
    private ShopSolrRepository shopSolrRepository;

    @Autowired
    private WxUserRentShopService wxUserRentShopService;

    @Autowired
    private ShoppingCarService shoppingCarService;

    @Autowired
    private VipShopService vipShopService;

    @Autowired
    @Lazy
    private SeckillService seckillService;

    @Autowired
    private ModulesProductsService modulesProductsService;


    @Override
    public List<ShopDTO> getByName(String name, int count, int status) {
        if (!("".equals(name.trim())) && name != null) {
//            var example = this.sqlBuilder().where(Sqls.custom()
//                    .andLike("shopname", "%" + name + "%")
//                    .andEqualTo("isdelete", 0)
//            );
//            if (status != -1) {
//                example = example.andWhere(Sqls.custom().andEqualTo("status", status));
//            }
//            if (count != -1) {
//                PageHelper.startPage(0, count);
//            }
            count = count == -1 ? Integer.MAX_VALUE : count;
            List ids = new ArrayList<String>();

            if (StringUtils.isEmpty(name)) {
                name = "*";
            }
            var q = String.format("shopname:(%s OR *%s*) ", name, name);
            if (status != -1) {
                q += String.format("AND status:%s", status);
            }

            try {
                ids = this.shopSolrRepository.queryByName(String.format("shopname:(%s OR *%s*)", name, name), null, new PageParameter(1, count), null).getList();
                if (CollectionUtils.isEmpty(ids)) {
                    return null;
                }
            } catch (Exception e) {
                this.log.error("Solr 查询失败!");
                e.printStackTrace();
            }


            List<TbShop> tbShops = this.listByIds(ids);
            return this.beanListMap(tbShops, this.currentDTOClass());
        }
        return null;
    }

    @Override
    public Pagination<ScoreProductMinDTO> getScoreShopByName(String name, PageParameter page, int status) {
        if (StringUtils.isEmpty(name)) {
            name = "*";
        }
        var q = String.format("shopname:(%s OR *%s*) AND isscore:1 ", name, name);
        if (status != -1) {
            q += String.format("AND status:%s", status);
        }
        List ids = new ArrayList<String>();

        try {
            Map<String, SolrQuery.ORDER> stringORDERMap = new HashMap<>();
            stringORDERMap.put("sort",SolrQuery.ORDER.desc);
            ids = this.shopSolrRepository.queryByName(q, page,stringORDERMap).getList();
        } catch (Exception e) {
            this.log.error("Solr 查询失败!");
            e.printStackTrace();
        }

        var p = new Pagination<ScoreProductMinDTO>();
        p.setPageIndex(page.getPageIndex());
        p.setPageSize(page.getPageSize());

        if (!CollectionUtils.isEmpty(ids)) {
            var list = this.listByIds(ids);
            p.setList(list);

        }
        return p;

    }

    @Override
    public Pagination<ShopDTO> getSaleShopByName(String name, PageParameter page, int status) throws Exception {
        if (StringUtils.isEmpty(name)) {
            name = "*";
        }
        List<ShopDTO> tbShops = null;
        var q = String.format("shopname:(%s OR *%s*) AND issale:1 ", name, name);
        if (status != -1) {
            q += String.format("AND status:%s", status);
        }


//        var q = String.format("shopname:(%s OR *%s*) ", name, name);
//        List<String> filter = new ArrayList<>() {{
//            add("issale:1");
//        }};
//        if (status != -1) {
//            filter.add(String.format("status:%s", status));
//        }
        var searchResp = this.shopSolrRepository.queryByName(q, null, page, null);
//        var searchResp = this.shopSolrRepository.queryByName(q, (String[]) filter.toArray(), page,null);
        var p = new Pagination<ShopDTO>(page.getPageIndex(), page.getPageSize(), searchResp.getTotal());
        if (searchResp.getTotal() != 0) {
            tbShops = this.listByIds(searchResp.getList());
            p.setList(tbShops);
        } else {
            var solr_pages = searchResp.getTotalPages();
            var startPageIndex = page.getPageIndex() - solr_pages;
            var result = this.gameCategoryService.queryShopsByCategoryName(new PageParameter(startPageIndex, page.getPageSize()), "%" + name + "%");
            return result;
        }
        return p;
    }

    @Override
    public Pagination<ShopDTO> getRentShopByName(String name, String type, PageParameter page, int status) throws Exception {
        if (StringUtils.isEmpty(name)) {
            name = "*";
        }
        List<ShopDTO> tbShops = null;
        var q = String.format("shopname:(%s OR *%s*) AND isrent:1 ", name, name);
        if (status != -1) {
            q += String.format("AND status:%s", status);
        }
        if (type.equalsIgnoreCase("soft_rent")) {
            q += " AND genres:1";
        } else if (type.equalsIgnoreCase("machine_rent")) {
            q += " AND genres:2";
        }

        var searchResp = this.shopSolrRepository.queryByName(q, null, page, null);
        var p = new Pagination<ShopDTO>(page.getPageIndex(), page.getPageSize(), searchResp.getTotal());
        tbShops = this.listByIds(searchResp.getList());
        p.setList(tbShops);
        return p;
    }


    @Override
    public ShopDTO getShopInfoById(String id) {
        var dbo = this.baseMapper.queryByid(id);
        return this.beanMapper.map(dbo, this.currentDTOClass());
    }

    @Override
    public ShopDTO getShopInfo(String wxUserId, String shopid, String sid) {
        var shop = this.getById(shopid);
        //是否是活动商品，是:加载具体活动
        if (StringUtils.isNotEmpty(sid)) {
            var activity = this.seckillService.getById(sid);
            shop.setSeckilltype(activity.getType());

            //会员特价信息
            var vip_shop = this.vipShopService.getOne(new HashMap<String, Object>() {{
                put("shopid", shopid);
                put("status", 1);
                put("activityid", sid);
                put("isdelete", 0);
            }});
            shop.setVipShop(vip_shop);
        }

        //当前用户已经有租赁的商品了，不允许再租赁
//        var hasRentRecord = this.wxUserRentShopService.isExistValidRentByWxUserId(wxUserId);
//        if (hasRentRecord != null) {
//            shop.setIsrent(0);
//        }
        return shop;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean addOrUpdateShop(ShopDTO entity) {
        var isEdit = StringUtils.isEmpty(entity.getId());
        if (StringUtils.isEmpty(entity.getId())) {
            entity.setId(IDUtils.genUUID());
            entity.setIsdelete(0);
            entity.setAddtime(new Date());
//                this.add(entity);
        }

        List categories = new ArrayList<TbGameCategory>();
        List tags = new ArrayList<TbGameTag>();


        if (entity.getCategoryList() != null && entity.getCategoryList().size() > 0) {
            for (var element : entity.getCategoryList()) {
                TbGameCategory gameCategory = new TbGameCategory();
                gameCategory.setAddtime(new Date());
                gameCategory.setIsdelete(0);
                gameCategory.setCategoryid(element.getId());
                gameCategory.setShopid(entity.getId());
                gameCategory.setId(IDUtils.genUUID());
                gameCategory.setAddtime(new Date());
//                    this.gameCategoryService.insert(gameCategory);
                categories.add(gameCategory);

            }
        }
        if (entity.getTagList() != null && entity.getTagList().size() > 0) {
            for (var element : entity.getTagList()) {
                TbGameTag gameTag = new TbGameTag();
                gameTag.setAddtime(new Date());
                gameTag.setIsdelete(0);
                gameTag.setTagid(element.getId());
                gameTag.setShopid(entity.getId());
                gameTag.setId(IDUtils.genUUID());
                gameTag.setAddtime(new Date());
//                    this.gameTagService.insert(gameTag);
                tags.add(gameTag);

            }
        }

        boolean result = this.transactionUtils.transact((a) -> {
            if (isEdit) {
                this.add(entity);
            } else {
                this.updateById(entity);
                //更新栏目中的商品数据
                this.modulesProductsService.updateShopPrice(entity);
            }

            //消息队列，异步写入引擎
            this.rabbitTemplate.convertAndSend(SHOP_SOLR_MASTER_EX, SHOP_SOLR_SAVE_QUEUE, entity, new CorrelationData(IDUtils.genUUID()));

            Example exampleGameTag = Example.builder(TbGameTag.class).where(Sqls.custom().andEqualTo("shopid", entity.getId())).build();
            this.gameTagService.delete(exampleGameTag);
            Example exampleGameCategory = Example.builder(TbGameCategory.class).where(Sqls.custom().andEqualTo("shopid", entity.getId())).build();
            this.gameCategoryService.delete(exampleGameCategory);

            if (tags != null && tags.size() > 0) this.gameTagService.addBatch(tags);

            if (categories != null && categories.size() > 0) this.gameCategoryService.addBatch(categories);

        });

        return result;
    }

    @Override
    public List<ShopDTO> getNewShopes(PageParameter page) {
        return this.page(page, new HashMap<String, Object>() {{
            put("isnew", 1);
            put("isdelete", 0);
            put("status", 1);
            put("issale", 1);
        }}, null, null).getList();
    }

    @Override
    public List<ShopDTO> getHotShopes(PageParameter page) {
        return this.page(page, new HashMap<String, Object>() {{
            put("ishot", 1);
            put("isdelete", 0);
            put("status", 1);
            put("issale", 1);
        }}, null, null).getList();
    }

    @Override
    public List<ShopDTO> getDiscountShopes(PageParameter page) {
        //折扣不为1，不能是下架的，不能是删除的
        var example = this.sqlBuilder().where(Sqls.custom()
                .andNotEqualTo("discount", 1)
                .andEqualTo("status", 1)
                .andEqualTo("issale", 1)
                .andEqualTo("isdelete", 0));
        return this.page(page, example.build()).getList();
    }

    @Override
    public Pagination<ShopDTO> getPageByCategory(String cid, PageParameter page) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<TbShop> list = null;
        if (cid.equalsIgnoreCase(IDUtils.empty())) {
            var example = this.sqlBuilder().where(Sqls.custom()
                    .andEqualTo("isdelete", 0)
                    .andEqualTo("issale", 1)
                    .andEqualTo("status", 1)
            ).orderByDesc("addtime").build();
            list = (Page) this.baseMapper.selectByExample(example);
        } else {
            list = (Page) this.baseMapper.listByCategory(cid);
        }
        PageInfo<TbShop> pageInfo = new PageInfo<TbShop>(list);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

    //商品上下架
    @Override
    public boolean editStatus(ShopDTO shopDTO) {
        boolean result = this.transactionUtils.transact((a) -> {
            TbShop tbShop = this.baseMapper.selectByPrimaryKey(shopDTO.getId());
            //如果没有商品上下架值，则默认为上架
            tbShop.setStatus(shopDTO.getStatus() != null ? shopDTO.getStatus() : 1);
            this.baseMapper.updateByPrimaryKey(tbShop);
            //下线删除商品
            if (tbShop.getStatus() == 0) this.modulesProductsService.delProduct(shopDTO.getId());
            this.rabbitTemplate.convertAndSend(SHOP_SOLR_MASTER_EX, SHOP_SOLR_SAVE_QUEUE, tbShop, new CorrelationData(IDUtils.genUUID()));
            boolean b = shoppingCarService.removeShopByShopId(shopDTO.getId());
        });
        return result;
    }

    @Override
    public Pagination<ScoreProductMinDTO> getScoreShopes(PageParameter page) {
        var pageInfo = this.page(page, new HashMap<String, Object>() {{
            put("isscore", 1);
            put("status", 1);
            put("isdelete", 0);

        }}, ObjectUtils.toArray("shopname"), 1);

        return new Pagination<>(pageInfo, this.beanListMap(pageInfo.getList(), ScoreProductMinDTO.class));
    }

    /**
     * 检测商品名是否重复
     *
     * @param id
     * @param name
     * @return
     */
    @Override
    public boolean checkSimilarName(String id, String name) {
        var example = this.sqlBuilder().where(Sqls.custom()
                .andEqualTo("shopname", name.trim())
                .andEqualTo("isdelete", 0));
        if (StringUtils.isNotEmpty(id)) {
            example = example.andWhere(Sqls.custom()
                    .andNotEqualTo("id", id));
        }

        int i = this.baseMapper.selectCountByExample(example.build());
        return i > 0 ? false : true;

    }

    //商品删除 删除关联表数据
    @Override
    public boolean shopDeleteById(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.deleteById(id);
            this.rabbitTemplate.convertAndSend(SHOP_SOLR_MASTER_EX, SHOP_SOLR_DELETE_QUEUE, id, new CorrelationData(IDUtils.genUUID()));
            Example exampleGameTag = Example.builder(TbGameTag.class).where(Sqls.custom().andEqualTo("shopid", id)).build();
            this.gameTagService.delete(exampleGameTag);
            Example exampleGameCategory = Example.builder(TbGameCategory.class).where(Sqls.custom().andEqualTo("shopid", id)).build();
            this.gameCategoryService.delete(exampleGameCategory);
            this.modulesProductsService.delProduct(id);
        });
        return result;
    }

//    @Override
//    public Pagination<ProductMinDTO.VipProductMinDTO> getSecKillShops(PageParameter page, WxUserDTO wxUserDTO) {
//        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
//        Page list = (Page) this.baseMapper.getVipShops(wxUserDTO.getVipid() > 0);
//        var pagination = new Pagination<ProductMinDTO.VipProductMinDTO>(page.getPageIndex(), page.getPageSize(), (int) list.getTotal());
//        pagination.setList(this.beanListMap(list, ProductMinDTO.VipProductMinDTO.class));
//        return pagination;
//    }

    @Override
    public List<VipProductMinDTO> getSecKillShops(String sid) {
        var list = this.baseMapper.getVipShopsBySId(sid);
        return this.beanListMap(list, VipProductMinDTO.class);
    }


    @Override
    public ShopDTO getVipShopById(String sid, String proId) {
        var dto = this.baseMapper.getShopWithVipById(sid, proId);
        return this.beanMap(dto, this.currentDTOClass());
//         this.beanMapper.map(dto, this.currentDTOClass());
    }

    /**
     * 按条件查询
     *
     * @param page    分页
     * @param filters 条件
     * @param sorts   排序字段
     * @param isAsc   排序
     * @return
     */
    @Override
    public Pagination<ShopDTO> query(PageParameter page, Map<String, Object> filters, String[] sorts, Integer[] isAsc) throws IOException, SolrServerException {
//        Example exampleShop = Example.builder(TbShop.class).where(Sqls.custom().andLike("shopname", "%" + filters.get("shopname").toString() + "%")).build();
//        return this.page(page, exampleShop);
        var keyword = filters.get("shopname").toString();
        var q = String.format("shopname:(%s OR *%s*)", keyword, keyword);

        Map<String, SolrQuery.ORDER> stringORDERMap = new HashMap<>();
        stringORDERMap.put("sort",SolrQuery.ORDER.desc);

        var ids = this.shopSolrRepository.queryByName(keyword, page,stringORDERMap);
        //未找到对应的数据，则从商城分类中查找

        var paginate = new Pagination<ShopDTO>(page.getPageIndex(), page.getPageSize(), ids.getTotal());
        if (ids.getTotal() == 0) {

            return paginate;
        }

        var list = this.listByIds(ids.getList());
        paginate.setList(list);
        return paginate;
    }

    @Override
    @CacheEvict(allEntries = true)
    public boolean deleteById(String id) {
        return super.deleteById(id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean removeById(String id) {
        return super.removeById(id);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean addOrUpdate(ShopDTO entity) {
        return super.addOrUpdate(entity);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean add(ShopDTO entity) {
        return super.add(entity);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean update(ShopDTO entity, Map<String, Object> columnMap) {
        return super.update(entity, columnMap);
    }

    @CacheEvict(allEntries = true)
    @Override
    public boolean updateById(ShopDTO entity) {
        return super.updateById(entity);
    }
}
