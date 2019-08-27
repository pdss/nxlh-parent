package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbModulesProductsMapper;
import com.nxlh.manager.model.dbo.TbModulesProducts;
import com.nxlh.manager.model.dto.ModulesProductsDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.service.ModulesProductsService;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service(interfaceClass = ModulesProductsService.class)
public class ModulesProductsServiceImpl extends BaseDbCURDSServiceImpl<TbModulesProductsMapper, TbModulesProducts, ModulesProductsDTO> implements ModulesProductsService {


    @Override
    public Pagination<ModulesProductsDTO> page(PageParameter page, String moduleId) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<ModulesProductsDTO> tbModulesProduts = this.baseMapper.queryPage(moduleId);
        PageInfo<ModulesProductsDTO> pageInfo = new PageInfo<ModulesProductsDTO>(tbModulesProduts);
        return this.mapPageInfo(pageInfo, this.currentDTOClass());
    }

    @Override
    public boolean updateShopPrice(ShopDTO entity) {
        Example example = new Example(TbModulesProducts.class);
        example.createCriteria().andEqualTo("productid", entity.getId());
        TbModulesProducts model = new TbModulesProducts();
        model.setPrice(entity.getSaleprice());
        model.setNowprice(entity.getSaleprice());
        model.setProductname(entity.getShopname());
        model.setThumbnail(entity.getThumbnails());
        int i = this.baseMapper.updateByExampleSelective(model, example);
        return i > 0 ? true : false;
    }

    @Override
    public boolean checkProduct(ModulesProductsDTO entity) {
        if (!StringUtils.isEmpty(entity.getId())) {
            return true;
        }
        Example example = new Example(TbModulesProducts.class);
        example.createCriteria().andEqualTo("moduleid", entity.getModuleid()).andEqualTo("productid", entity.getProductid()).andEqualTo("isdelete", 0);
        List<TbModulesProducts> tbModulesProducts = this.baseMapper.selectByExample(example);
        return tbModulesProducts.size() > 0 ? false : true;
    }

    @Override
    public boolean delProduct(String id) {
        Example example = new Example(TbModulesProducts.class);
        example.createCriteria().andEqualTo("productid", id);
        TbModulesProducts model = new TbModulesProducts();
        model.setIsdelete(1);
        int i = this.baseMapper.updateByExampleSelective(model, example);
        return i > 0 ? true : false;
    }
}
