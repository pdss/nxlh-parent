package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbBannerMapper;
import com.nxlh.manager.model.dbo.TbBanner;
import com.nxlh.manager.model.dto.BannerDTO;
import com.nxlh.manager.model.dto.MenuDTO;

import java.util.List;
import java.util.Map;

public interface BannerService extends BaseService<BannerDTO, TbBannerMapper, TbBanner> {

    /**
     * 图片是否启用
     * @param bannerDTO
     * @return
     */
    boolean editStatus(BannerDTO bannerDTO);


    /**
     * 获取上架的banner
     * @return
     */
    Map<Integer,List<BannerDTO>> getValidBanners();

//     List<BannerDTO> getValidBanners();
}
