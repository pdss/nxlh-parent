package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.manager.mapper.TbWxuserAddressMapper;
import com.nxlh.manager.model.dbo.TbWxuserAddress;
import com.nxlh.manager.model.dto.WxUserAddressDTO;
import com.nxlh.manager.service.WxUserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.List;

@Service(interfaceClass = WxUserAddressService.class)
@Slf4j
public class WxUserAddressServiceImpl extends BaseDbCURDSServiceImpl<TbWxuserAddressMapper, TbWxuserAddress, WxUserAddressDTO> implements WxUserAddressService {


    @Override
    public List<WxUserAddressDTO> getByWxUId(String wxUid) {
        return this.list(new HashMap<String, Object>() {{
            put("wxuserid", wxUid);
            put("isdelete", 0);
        }}, ObjectUtils.toArray("isdefault"), 0);
    }

    @Override
    public WxUserAddressDTO getDefaultAddr(String wxUid, boolean getFirst) {
        var address = this.getByWxUId(wxUid);
        if (CollectionUtils.isNotEmpty(address)) {
            var defaultAddr = address.stream().filter(e -> e.getIsdefault() == 1).findFirst();
            if (defaultAddr.isPresent()) {
                return defaultAddr.get();
            } else {
                return address.get(0);
            }
        }
        return null;
    }

    @Override
    public boolean addOrUpdateAddress(WxUserAddressDTO addressDTO) {
        if (addressDTO.getIsdefault() == 1) {
            this.removeDefaultAddrByUserId(addressDTO.getWxuserid());
        }
        return this.addOrUpdate(addressDTO);

    }

    @Override
    public Integer removeDefaultAddrByUserId(String wxUserId) {
        return this.baseMapper.removeDefaultAddrByUserId(wxUserId);
    }

}
