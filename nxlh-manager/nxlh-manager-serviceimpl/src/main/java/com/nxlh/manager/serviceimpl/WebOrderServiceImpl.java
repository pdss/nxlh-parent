package com.nxlh.manager.serviceimpl;


import com.alibaba.dubbo.config.annotation.Service;
import com.dyuproject.protostuff.Message;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.mapper.TbWebOrderMapper;
import com.nxlh.manager.mapper.TbWxuserRecyclesMapper;
import com.nxlh.manager.model.dbo.TbWebOrder;
import com.nxlh.manager.model.dbo.TbWxuserRecycles;
import com.nxlh.manager.model.dto.WebOrderDTO;
import com.nxlh.manager.model.dto.WxUserRecycleDTO;
import com.nxlh.manager.service.WebOrderService;
import com.nxlh.manager.service.WxUserRecycleService;
import com.nxlh.manager.service.WxUserService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.util.Sqls;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service(interfaceClass = WebOrderService.class)
public class WebOrderServiceImpl extends BaseDbCURDSServiceImpl<TbWebOrderMapper, TbWebOrder, WebOrderDTO> implements WebOrderService {

    @Autowired
    private WxUserService wxUserService;

    private static Object locker = new Object();

    @Override
    public MessageModel sync(Map<String, Object> model) {

        var items = (List<Map<String, Object>>) model.get("OrderItems");
        var dbos = new ArrayList<WebOrderDTO>();
        for (var e : items) {
            var id = e.get("Id");
            //库中是否已记录过当前订单项
            var exist_item = this.count(this.sqlBuilder().andWhere(Sqls.custom().andEqualTo("orderitemid", id)).build()) > 0;
            //该项目已存在,忽略
            if (!exist_item) {
                var order = new WebOrderDTO();
                order.setId(IDUtils.genUUID());
                order.setIsdelete(0);
                order.setAddtime(new Date());
                order.setItempayprice(new BigDecimal(e.get("SumPrice2").toString()));
                order.setOrderid(Long.parseLong(e.get("OrderId").toString()));
                order.setOrderitemid(Long.parseLong(e.get("Id").toString()));
                order.setOrderno(e.get("OrderNo").toString());
                order.setOrderpayprice(new BigDecimal(model.get("PayPrice").toString()));
                order.setOrderprice(new BigDecimal(model.get("OrderPrice").toString()));
                order.setOrdertype(Integer.parseInt(model.get("OrderType").toString()));
                order.setOrdertype2(Integer.parseInt(model.get("OrderType2").toString()));
                order.setStatus(Integer.parseInt(model.get("Status").toString()));
                order.setUnionid(model.get("UnionId").toString());
                dbos.add(order);
            }
        }


        if (dbos.size() > 0) {
            var res = this.transactionUtils.transact((a) -> {
                this.addBatch(dbos);
            });
            if (res) {
                return MessageModel.ok("ok");
            }
        }
        return MessageModel.error("error");


    }


    /**
     * 处理网站积分兑换
     *
     * @param model
     * @return
     */
    @Override
    public MessageModel syncScore(Map<String, Object> model) {

        synchronized (locker) {

            var exists = this.count(this.sqlBuilder().where(Sqls.custom().andEqualTo("orderno", model.get("OrderNo"))).build()) > 0;
            //防重复
            if (exists) {
                return MessageModel.error("exists");
            }

            //通过Unionid找用户
            var user = this.wxUserService.getOne(new HashMap<String, Object>() {{
                put("unionid", model.get("UnionId"));
            }});
            if (user == null) {
                return MessageModel.error("user invalid");
            }

            user.setVscore(user.getVscore().subtract(new BigDecimal(model.get("Score").toString())));
            //积分不足
            if (user.getVscore().compareTo(BigDecimal.ZERO) == -1) {
                return MessageModel.ok(new HashMap<String, Object>() {{
                    put("SyncResult", false);
                    put("Score", 0);
                }});
            }

            var order = new WebOrderDTO();
            order.setId(IDUtils.genUUID());
            order.setIsdelete(0);
            order.setAddtime(new Date());
            order.setOrdertype(Integer.parseInt(model.get("OrderType").toString()));
            order.setOrderno(model.get("OrderNo").toString());
            order.setOrderid(Long.parseLong(model.get("Id").toString()));
            order.setStatus(1);
            order.setOrderprice(new BigDecimal(model.get("Score").toString()));
            order.setItempayprice(new BigDecimal(model.get("Score").toString()));
            order.setOrderpayprice(new BigDecimal(model.get("Score").toString()));
            order.setOrderitemid(Long.parseLong(model.get("ItemId").toString()));
            order.setUnionid(model.get("UnionId").toString());

            var res = this.transactionUtils.transact((a) -> {
                this.add(order);
                this.wxUserService.updateById(user);
            });
            if (res) {
                return MessageModel.ok(new HashMap<String, Object>() {{
                    put("SyncResult", true);
                    put("Score", user.getVscore());
                    put("VipType", user.getVipid());
                }});
            } else {
                return MessageModel.error("fail");
            }

        }
    }

    @Override
    public MessageModel close(String orderno) {
        var order = this.list(new HashMap<String, Object>() {{
            put("orderno", orderno);
        }}, null, null).get(0);

        order.setStatus(4);
        this.updateById(order);
        return MessageModel.ok();

    }

    @Autowired
    private TbWebOrderMapper webOrderMapper;


//    @Override
//    public Boolean refundByIds(List<Integer> list) {
//        return webOrderMapper.refundByIds(list);
//    }

    @Override
    public Boolean refundById(Integer id) {
        var example = this.sqlBuilder()
                .where(Sqls.custom()
                        .andEqualTo("orderitemid", id)).build();
        List<TbWebOrder> tbWebOrders = this.webOrderMapper.selectByExample(example);
        if (tbWebOrders.size() > 0) {
            return webOrderMapper.refundByIds(tbWebOrders.stream().map(e -> e.getId()).collect(Collectors.toList()));
        }
        return false;
    }

    @Override
    public List<WebOrderDTO> getOverOrderByDate(Date startDate) {
        return this.webOrderMapper.getOverOrderByDate(startDate);
    }

    @Override
    public Boolean CloseByIds(List<Long> ids) {
        var example = this.sqlBuilder()
                .where(Sqls.custom()
                        .andIn("id", ids)).build();
        List<TbWebOrder> tbWebOrders = this.webOrderMapper.selectByExample(example);
        List<Long> collect = tbWebOrders.stream().map(e -> {
            return e.getOrderitemid();
        }).distinct().collect(Collectors.toList());
        return this.CloseByOrderIds(collect);
    }


    @Override
    public Boolean CloseByOrderIds(List<Long> list) {
        return this.webOrderMapper.CloseByIds(list);
    }

    @Override
    public List<WebOrderDTO> getOverOrderByIds(List<Long> list) {
        return this.webOrderMapper.getOverOrderByIds(list);
    }
}
