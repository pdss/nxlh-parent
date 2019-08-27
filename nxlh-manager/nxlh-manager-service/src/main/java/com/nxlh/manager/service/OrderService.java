package com.nxlh.manager.service;

import com.aliyuncs.exceptions.ClientException;
import com.dyuproject.protostuff.Message;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbOrderMapper;
import com.nxlh.manager.model.dbo.TbOrder;
import com.nxlh.manager.model.dto.OrderDTO;
import com.nxlh.manager.model.enums.ExpressEnums;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.model.extend.OrderMinDTO;
import com.nxlh.manager.model.extend.PayOrderDTO;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public interface OrderService extends BaseService<OrderDTO, TbOrderMapper, TbOrder> {

    /**
     * 订单详情,数据库中实时数据
     *
     * @param orderid
     * @return
     */
    OrderDTO getDetails(String orderid);


    /**
     * 订单详情，实时订单状态
     *
     * @return
     */
    OrderDTO getOrderInfoWithStatus(String orderid);

    /**
     * 发货
     *
     * @param id            id
     * @param express       快递，详见 ExpressEnums
     * @param trackernumber 快递单号
     * @return
     */
    MyResult transit(String id, int express, String trackernumber);


    /**
     * 根据特定分类获取订单列表(分页)
     *
     * @param page   分页参数
     * @param filter wait refund  yes
     * @return
     */
    MyResult getByFilter(PageParameter page, String filter);


    /**
     * 同意退款
     *
     * @param orderRefundId 退款记录id
     * @param refundPrice   退款金额
     * @return
     */
    MyResult accessRefund(String orderRefundId, BigDecimal refundPrice) throws WxPayException;


    /**
     * 拒绝退款
     *
     * @param orderRefundId 退款记录id
     * @param refundReason  拒绝原因
     * @return
     */
    MyResult rejectRefund(String orderRefundId, String refundReason);


    /**
     * 自动关闭订单，默认8天
     */

    boolean autoCloseOrder();

    /**
     * 自动确认收货，默认8天
     *
     * @return
     */
    boolean autoConfirmReceiveOrder();


    /**
     * 预生成订单信息
     *
     * @return
     */
    MyResult preview(String wxUserId, Map<String, Integer> products);

    /**
     * 秒杀 预生成订单
     *
     * @param wxUserId
     * @return
     */
    MyResult previewByMS(String wxUserId, String sid, String productid);


    /**
     * 获取预览订单
     *
     * @param orderno
     * @return
     */
    MyResult getPreview(String orderno);


    /**
     * 支付订单(微信-统一下单)
     *
     * @return
     */
    MyResult payOrder(String wxUserId, PayOrderDTO order) throws WxPayException;


    /**
     * 微信支付回调通知处理
     *
     * @param data
     * @return
     */
    boolean payNotify(String data) throws WxPayException;


    /**
     * 秒杀订单通知处理
     *
     * @param data
     * @return
     */
    boolean secKillNotify(String data) throws WxPayException;


    /**
     * 获取指定时间段的总消费金额
     *
     * @param wxUserId
     * @param start
     * @param end
     * @return
     */
    BigDecimal getSumPriceInTime(String wxUserId, Date start, Date end);


    /**
     * 分页获取订单列表，简略显示订单信息
     *
     * @return
     */
    Pagination<OrderMinDTO> getMinListByPage(String wxUserId, PageParameter page, Map<String, Object> filters);

    /**
     * 申请退款
     *
     * @return
     */
    MyResult orderRefund(String orderId, OrderEnums.OrderRefundReasonEnum reason, String remark, BigDecimal price);


    /**
     * 积分兑换商品
     *
     * @param wxUserId
     * @param productId
     * @return
     */
    MyResult exchangeOrder(String wxUserId, String productId);

    /**
     * 确认兑换订单
     *
     * @param order
     * @return
     * @throws WxPayException
     */
    MyResult confirmExOrder(PayOrderDTO order) throws WxPayException;


    /**
     * 创建excel表格
     *
     * @param startDate
     * @param endDate
     * @return
     * @throws IOException
     */
    List<HashMap<String, Object>> createExcelByOrder(Date startDate, Date endDate) throws IOException;


    /**
     * 根据订单id获取退款订单
     *
     * @param id
     * @return
     */
    MyResult getRefundByOrderId(String id);

    /**
     * 确认收货
     *
     * @param orderid
     * @return
     */
    MyResult confirmReceive(String orderid, OrderEnums.OrderConfirmTypeEnum confirmTypeEnum);


    /**
     * 租赁商品
     *
     * @param term 周期  1月 2季度 3半年 4一年
     * @return
     */
    MyResult rentOrder(String wxUserId, String productId) ;


    /**
     * 归还出租订单
     *
     * @return
     */
    MyResult refundRentOrder(String orderId, BigDecimal refundprice) throws WxPayException;

    /**
     * 获取微信用户的各订单状态的数量
     *
     * @return
     */
    Map<String, Long> getCountGroupByStatus(String wxUserId);


    /**
     * 时间段内是否有订单
     *
     * @param dates
     * @param wxUserId
     * @return
     */
    Boolean hasFewMonthOrder(Map<Date, Date> dates, String wxUserId);

    /**
     * 检查秒杀商品数量
     *
     * @param productId
     * @return
     */
    Boolean checkMSStock(String wxUserId, String sid, String productId);

    void UpdateAccountLevel(List<OrderDTO> orders);

    /**
     * 提醒用户出租商品即将到期
     *
     * @return
     */
    boolean remindLeaseExpire() throws ClientException;


    /**
     * 关闭网站订单
     * @param ids
     * @return
     */
    boolean closeWebOrder(List<Long> ids);

}
