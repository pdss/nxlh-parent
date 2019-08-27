package com.nxlh.manager.service;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbWxuserRecyclesMapper;
import com.nxlh.manager.model.dbo.TbWxuserRecycles;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import com.nxlh.manager.model.enums.AccoutEnums;

public interface WxUserRecycleService extends BaseService<WxUserRecycleDTO, TbWxuserRecyclesMapper, TbWxuserRecycles> {

    Pagination<WxUserRecycleDTO> listByPage(PageParameter page, String type, String userid);

    /**
     * 根据id取消订单
     *
     * @param id
     * @return
     */
    boolean cancel(String id);

    /**
     * 根据id完成订单
     *
     * @param id
     * @return
     */
    boolean close(String id);

    boolean checkPrice(WxUserRecycleDTO model);
    /**
     * 寄送
     * @param orderId
     * @param express
     * @param expressno
     * @return
     */
    boolean send(String orderId,String express,String expressno, String account, AccoutEnums.AccoutTypeEnums type);
}
