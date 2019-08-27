package com.nxlh.manager.service;

import com.nxlh.common.model.MessageModel;
import com.nxlh.manager.mapper.TbWebOrderMapper;
import com.nxlh.manager.model.dbo.TbWebOrder;
import com.nxlh.manager.model.dto.WebOrderDTO;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface WebOrderService extends BaseService<WebOrderDTO, TbWebOrderMapper, TbWebOrder> {

//    /**
//     * 根据id集合退货
//     *
//     * @param list
//     * @return
//     */
//    Boolean refundByIds(List<Integer> list);

    Boolean refundById(Integer id);

    /**
     * 获取指定日期内的关闭订单
     *
     * @param startDate
     * @return
     */
    List<WebOrderDTO> getOverOrderByDate(Date startDate);

    Boolean CloseByIds(List<Long> ids);

    Boolean CloseByOrderIds(List<Long> list);

    List<WebOrderDTO> getOverOrderByIds(List<Long> list);

    //支付订单
    MessageModel sync(Map<String, Object> model);


    //处理网站积分兑换
    MessageModel syncScore(Map<String, Object> model);

    //订单关闭
    MessageModel close(String orderno);


}
