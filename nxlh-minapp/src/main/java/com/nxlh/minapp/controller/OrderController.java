package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyResponse;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.google.common.util.concurrent.RateLimiter;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.model.dto.OrderDTO;
import com.nxlh.manager.model.enums.OrderEnums;
import com.nxlh.manager.model.extend.PayOrderDTO;
import com.nxlh.manager.service.OrderItemService;
import com.nxlh.manager.service.OrderRefundService;
import com.nxlh.manager.service.OrderService;
import com.nxlh.manager.service.WebOrderService;
import com.nxlh.minapp.model.OrderRefundVO;
import com.nxlh.minapp.model.order.PreviewOrderVO;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@ApiController("/api/wx/order")
public class OrderController extends BaseController {

    @Reference
    private OrderService orderService;


    @Reference
    private OrderItemService orderItemService;

    @Reference
    private OrderRefundService orderRefundService;


    private static RateLimiter limiter = RateLimiter.create(20.0);

    /**
     * 积分商品兑换，预生成订单
     *
     * @return
     */
    @GetMapping("exchange")
    public MyResult exchange(@RequestParam String id) throws UnAuthorizeException {
        return this.orderService.exchangeOrder(this.getUserId(), id);
    }

    /**
     * 预生成订单
     *
     * @return
     */
    @PostMapping("preview")
    public MyResult preview(@RequestBody PreviewOrderVO req) throws UnAuthorizeException {
        if (CollectionUtils.isEmpty(req.getProducts())) {
            return badreq("下单数据无效");
        }
        var products = new HashMap<String, Integer>();
        req.getProducts().forEach(e -> {
            products.put(e.getProductId(), e.getCount());
        });
        return this.orderService.preview(this.getUserId(), products);
    }

    /**
     * 秒杀 预生成订单
     *
     * @return
     */
    @PostMapping("mspreview")
    public MyResult previewByMS(@RequestBody Map<String, Object> params) throws UnAuthorizeException {
        if (limiter.tryAcquire(1, TimeUnit.SECONDS)) {
            limiter.acquire();
            var proId = params.get("shopid").toString();
            var sid = params.get("sid").toString();

            return this.orderService.previewByMS(this.getUserId(), sid, proId);
        } else {
            return MyResult.forb("Sorry~客官!排队人数太多,请稍后重试~");
        }
    }


    /**
     * 获取预生成订单
     *
     * @param orderno
     * @return
     */
    @GetMapping("getpreview")
    public MyResult getPreview(@RequestParam String orderno) throws UnAuthorizeException {
        return this.orderService.getPreview(orderno);

    }


    /**
     * 确认兑换积分订单
     *
     * @return
     */
    @PostMapping("confirmorder")
    public MyResult confirmScoreOrder(@RequestBody PayOrderDTO req) throws WxPayException {
        return this.orderService.confirmExOrder(req);
    }


    /**
     * 微信支付
     *
     * @return
     */
    @PostMapping("payorder")
    public MyResult payOrder(@RequestBody PayOrderDTO req) throws Exception {
        return this.orderService.payOrder(this.getUserId(), req);

    }

    /**
     * 支付完成回调
     *
     * @param data
     * @return
     */
    @PostMapping("paynotify")
    public String paynotify(@RequestBody String data) throws WxPayException {
        var result = this.orderService.payNotify(data);
        if (result) {
            return WxPayNotifyResponse.success("Ok");
        }
        return WxPayNotifyResponse.fail("fail");
    }


    /**
     * 秒杀订单通知
     *
     * @return
     */
    @PostMapping("seckillnotify")
    public String secKillNotify(@RequestBody String data) throws WxPayException {
        var result = this.orderService.secKillNotify(data);
        if (result) {
            return WxPayNotifyResponse.success("Ok");
        }
        return WxPayNotifyResponse.fail("fail");
    }


    /**
     * 分页获取列表
     *
     * @return
     */
    @GetMapping("getlistbypage")
    public MyResult getListByPage(@RequestParam String type, @RequestParam(defaultValue = "0") Integer pageIndex) throws IllegalAccessException, UnAuthorizeException {
        var params = new HashMap<String, Object>() {{
            put("filter", type);
        }};
        var result = this.orderService.getMinListByPage(this.getUserId(), new PageParameter(pageIndex, this.pageSize), params);
        return MyResult.ok(result);
    }

    /**
     * 获取订单详情
     *
     * @return
     */
    @GetMapping("getdetails")
    public MyResult getDetails(@RequestParam String id) {
        var dto = this.orderService.getOrderInfoWithStatus(id);
        return ok(dto);
    }


    /**
     * 申请退款
     *
     * @return
     */
    @PostMapping("refund")
    public MyResult refund(@RequestBody OrderRefundVO req) {
        return this.orderService.orderRefund(req.getOrderid(),
                OrderEnums.OrderRefundReasonEnum.getValue(req.getRefundreason()),
                req.getRemark(),
                req.getRefundprice());
    }


    /**
     * 确认收货
     *
     * @return
     */
    @GetMapping("confirmreceive")
    public MyResult confirmReceiveOrder(@RequestParam String orderid) {
        if (StringUtils.isEmpty(orderid)) {
            return badreq("无效请求参数");
        }
        return this.orderService.confirmReceive(orderid, OrderEnums.OrderConfirmTypeEnum.manual);
    }


    /**
     * 获取订单项详情
     *
     * @return
     */
    @GetMapping("getorderiteminfo")
    public MyResult getOrderItemInfo(@RequestParam String id) {
        return this.orderItemService.getOrderItemDetail(id);

    }


    /**
     * 获取订单的退款项
     *
     * @return
     */
    @GetMapping("getorderrefunditems")
    public MyResult getOrderRefundItems(@RequestParam String id) {
        return this.orderRefundService.getOrderRefundItemsStatus(id);
    }


    /**
     * 租赁
     *
     * @return
     */
    @GetMapping("rentorder")
    public MyResult rentOrder(@RequestParam String id) throws UnAuthorizeException {
        if (StringUtils.isEmpty(id)) {
            return badreq("无效请求参数");
        }
        return this.orderService.rentOrder(this.getUserId(), id);
    }

    /**
     * 检查秒杀商品库存
     *
     * @return
     */
    @GetMapping("checkmsstock")
    public MyResult checkMSStock(@RequestParam String shopId, @RequestParam String sid) throws UnAuthorizeException {
        var result = this.orderService.checkMSStock(this.getUserId(), sid, shopId);
        return ok(result);
    }



}
