package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.mapper.TbIndexmodulesMapper;
import com.nxlh.manager.model.dbo.TbIndexmodules;
import com.nxlh.manager.model.dbo.TbModulesProducts;
import com.nxlh.manager.model.dto.IndexmodulesDTO;
import com.nxlh.manager.service.IndexmodulesService;
import com.nxlh.manager.service.ModulesProductsService;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service(interfaceClass = IndexmodulesService.class)
public class IndexmodulesServiceImpl extends BaseDbCURDSServiceImpl<TbIndexmodulesMapper, TbIndexmodules, IndexmodulesDTO> implements IndexmodulesService {

    @Autowired
    @Lazy
    private ModulesProductsService modulesProductsService;


    @Override
    public Map<String, List<IndexmodulesDTO>> indexModules() {

        String[] orderby = {"sort"};
        var level = this.list(this.sqlBuilder().where(Sqls.custom().andEqualTo("isdelete", 0)).build());

        var level1 = level.stream().filter(e -> e.getLevel() == 1).sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(toList());
        var level2 = level.stream().filter(e -> e.getLevel() == 2).sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(toList());
        var level3 = level.stream().filter(e -> e.getLevel() == 3).sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(toList());
        var discounts = level.stream().filter(e -> e.getLevel() == 4).sorted((a, b) -> a.getSort().compareTo(b.getSort())).collect(toList());

        if (CollectionUtils.isNotEmpty(level3)) {
            var level3_ids = level3.stream().map(e -> e.getId()).collect(toList());
            //所有三级下的关联商品
            var shops = this.modulesProductsService.list(Example.builder(TbModulesProducts.class).where(Sqls.custom().andIn("moduleid", level3_ids).andEqualTo("isdelete", 0)).orderByDesc("sort").build());
            if (shops != null) {
                level3.forEach(e -> {
                    e.setProducts(shops.stream().filter(s -> s.getModuleid().equalsIgnoreCase(e.getId())).limit(8).collect(toList()));
                });

            }

        }

        if (CollectionUtils.isNotEmpty(discounts)) {
            var discount = discounts.get(0);
            var shops = this.modulesProductsService.page(new PageParameter(0, 8), Example.builder(TbModulesProducts.class).where(Sqls.custom().andEqualTo("moduleid", discount.getId()).andEqualTo("isdelete", 0)).orderByDesc("sort").build());
            discount.setProducts(shops.getList());
        }
        return new HashMap<>() {{
            put("level1", level1);
            put("level2", level2);
            put("level3", level3);
            put("discount", discounts);
        }};
    }
}
