package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbOrder;
import com.nxlh.manager.model.dto.OrderDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TbOrderMapper extends Mapper<TbOrder> {

    /**
     * 订单详情
     *
     * @param id
     * @return
     */
    TbOrder getDetails(String id);

    /**
     * 根据开始日期和结束日期 获取指定时间段内所有订单详情
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<TbOrder> getAllDetails(@Param("startDate") String startDate, @Param("endDate") String endDate);


    /**
     * 待退款的订单
     *
     * @return
     */
    List<TbOrder> getWaitRefundOrders();


    /**
     * 所有订单详情(包括退款项)
     *
     * @return
     */
    List<TbOrder> getAllDetailOrders();


    /**
     * 获取指定时间段的消费总金额
     *
     * @param wxuserid
     * @param start
     * @param end
     * @return
     */
    BigDecimal getSumPriceInTime(@Param("wxuserid") String wxuserid, @Param("starttime") Date start, @Param("endtime") Date end);


    /**
     * 根据条件查询订单关联微信用户名称
     *
     * @param filter 可为null 查询条件
     * @param type   可为null 查询订单类型
     * @return
     */
    List<TbOrder> queryByFilter(@Param("filter") String filter, @Param("type") Integer type, @Param("ordertype") Integer ordertype);

    /**
     * 根据条件查询退款订单关联微信用户名称
     *
     * @param filter 可为null 查询条件
     * @param type   可为null 查询订单类型
     * @return
     */
    List<TbOrder> queryByRefundType(@Param("filter") String filter, @Param("type") Integer type, @Param("ordertype") Integer ordertype);

    /**
     * 获取指定状态的用户订单列表
     *
     * @param status
     * @param wxuserid
     * @return
     */
    List<TbOrder> getUserOrdersByStatus(@Param("status") Integer[] status, @Param("wxuserid") String wxuserid, @Param("orderby") String sortField);


    /**
     * 获取订单中所有商品总数
     *
     * @param orderid
     * @return
     */
    Integer getOrderSumProductCount(String orderid);

    /**
     * 修改订单和订单子集的状态
     *
     * @param status
     * @param refund
     * @param id
     * @return
     */
    boolean editOrderAndOrderitemStatus(@Param("status") Integer status, @Param("refund") Integer refund, @Param("id") String id);


    /**
     * 各状态的订单数量
     *
     * @param wxUserId
     * @return
     */
    List<Map<String, Long>> getCountGroupByOrderStatus(String wxUserId);


    /**
     * 连续三个月内是否有订单
     *
     * @param dates
     * @param wxuserid
     * @return
     */
    Long hasFewMonthOrder(@Param("dates") Map<Date, Date> dates, @Param("wxuserid") String wxuserid);

    /**
     * 获取到日期前3天的的所有出租订单
     *
     * @param startDate
     * @param endDate
     * @return
     */
    List<OrderDTO> getRentRecordByEndDate(@Param("startDate") Date startDate,@Param("endDate") Date endDate);


    /**
     * 获取逾期的租赁订单
     *
     * @param date
     * @return
     */
//    List<OrderDTO> getRentOverdue(@Param("date") Date date);

}