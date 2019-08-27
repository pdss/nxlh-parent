package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbBannerMapper;
import com.nxlh.manager.model.dbo.TbBanner;
import com.nxlh.manager.model.dto.BannerDTO;
import com.nxlh.manager.service.BannerService;
import tk.mybatis.mapper.util.Sqls;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(interfaceClass = BannerService.class)
public class BannerServiceImpl extends BaseDbCURDSServiceImpl<TbBannerMapper, TbBanner, BannerDTO> implements BannerService {

    @Override
    public boolean editStatus(BannerDTO bannerDTO) {
        TbBanner tbBanner = this.baseMapper.selectByPrimaryKey(bannerDTO.getId());
        //如果没有图片状态值，则使用默认不启用原则
        tbBanner.setStatus(bannerDTO.getStatus() != null ? bannerDTO.getStatus() : 0);
        this.baseMapper.updateByPrimaryKey(tbBanner);
        return true;
    }

    @Override
    public Map<Integer,List<BannerDTO>> getValidBanners() {
        var example = this.sqlBuilder().where(Sqls.custom().andEqualTo("status", 1).andEqualTo("isdelete", 0)).build();
        var banners = this.list(example);
        if(banners==null){
            return null;
        }
        var dtos = this.beanListMap(banners, this.currentDTOClass());
        return dtos.stream().collect(Collectors.groupingBy(e->e.getType()));

    }


//    @Override
//    public List<BannerDTO> getValidBanners() {
//        var example = this.sqlBuilder().where(Sqls.custom().andEqualTo("status", 1).andEqualTo("isdelete", 0)).build();
//        var banners = this.list(example);
//        return this.beanListMap(banners, this.currentDTOClass());
//    }

}
