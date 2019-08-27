package com.nxlh.manager.model.enums;

public class OrderEnums {
    /**
     * 确认收货类型
     */
    public enum OrderConfirmTypeEnum {

        manual(1, "手动确认"),
        auto(2, "系统自动确认");

        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderConfirmTypeEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }


    /**
     * 订单的退款标记
     */
    public enum OrderRefundFlagEnum {

        none(0, "无退款"),
        part(1, "部分退款"),
        all(2, "全部退款");

        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderRefundFlagEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }

    /*
 订单状态
 */
    public enum OrderStatusEnum {
        nopay(0, "待支付"),
        ready(1, "待发货"),
        transit(2, "待收货"),
        received(3, "已完成"),
        complete(4, "交易关闭"),
        refund(5, "退款中"),
        refundsuccess(6, "已退款");


        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderStatusEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static String getMessage(Integer value) {
            OrderStatusEnum[] orderTypeEnums = values();
            for (OrderStatusEnum orderStatusEnum : orderTypeEnums) {
                if (orderStatusEnum.getValue() == value) {
                    return orderStatusEnum.getMessage();
                }
            }
            return null;
        }
    }

    /**
     * 订单的购买类型
     */
    public enum OrderTypeEnum {

        sale(1, "消费订单"),
        score(2, "积分订单"),
        //        free(3, "免费赠送"),
        lease(4, "出租"),
        seckill(5, "秒杀订单"),
        discount(6,"特价"),
        prebuy(7,"预售");

        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderTypeEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static String getMessage(Integer value) {
            OrderTypeEnum[] orderTypeEnums = values();
            for (OrderTypeEnum orderTypeEnum : orderTypeEnums) {
                if (orderTypeEnum.getValue() == value) {
                    return orderTypeEnum.getMessage();
                }
            }
            return null;
        }

    }


    /**
     * 退款状态
     */
    public enum OrderRefundEnum {

        wait(0, "待处理"),
        allow(1, "同意"),
        refund(2, "拒绝");

        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderRefundEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }


    /**
     * 租赁状态
     */
    public enum RentOrderStatusEnum {

        wait(1, "待归还"),
        complete(2, "已归还"),
        buy(3, "未如期归还,自动买断");


        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        RentOrderStatusEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }


    /**
     * 申请退款的原因
     */
    public enum OrderRefundReasonEnum {
        reason1(0, "颜色/尺寸/参数不符"),
        reason2(1, "商品瑕疵"),
        reason3(2, "质量问题"),
        reason4(3, "少发/漏件"),
        reason5(4, "未按约定时间发货"),
        reason6(5, "其他"),

        reason7(6, "租赁有效期内归还商品");


        private int value;
        private String message;

        public int getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        OrderRefundReasonEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static OrderRefundReasonEnum getValue(int val) {
            for (var e : OrderRefundReasonEnum.values()) {
                if (e.getValue() == val) {
                    return e;
                }
            }
            return null;
        }
    }

}
