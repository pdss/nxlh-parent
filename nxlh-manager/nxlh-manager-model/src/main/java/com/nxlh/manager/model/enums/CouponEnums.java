package com.nxlh.manager.model.enums;

/**
 * 优惠券枚举
 */
public class CouponEnums {

    /**
     * 优惠券类型
     */
    public enum CouponTypeEnum {
        None(0, "无"),
        Limit(1, "满减券"),
        Free(2, "免费券");


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

        CouponTypeEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

    }

    /**
     * 优惠券使用类型
     */
    public enum CouponStatusEnum {
        Valid(0, "未使用"),
        Used(1, "已使用"),
        Expire(2, "已过期");


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

        CouponStatusEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static CouponStatusEnum getValue(int val) {
            for (var e : CouponStatusEnum.values()) {
                if (e.getValue() == val) {
                    return e;
                }
            }
            return null;
        }

    }


    //领取方式
    public enum CouponGetTypeEnum{
        Manual(1, "手动"),
        Auto(2,"系统自动发放");


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

        CouponGetTypeEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }


    /**
     * 券的到期类型
     */
    public enum CouponValidTypeEnum{
        month(1, "当月"),
        date(2,"指定日期"),
        days(3,"指定天数");



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

        CouponValidTypeEnum(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static CouponValidTypeEnum getValue(int val) {
            for (var e : CouponValidTypeEnum.values()) {
                if (e.getValue() == val) {
                    return e;
                }
            }
            return null;
        }

    }


    /**
     * 优惠券使用范围
     */
    public enum CouponShopScoreEnum{
        all("all", "全场"),
        shopid("","指定商品"),
        tag("tag","商品标签");



        private String value;
        private String message;

        public String getValue() {
            return value;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        CouponShopScoreEnum(String val, String msg) {
            this.value = val;
            this.message = msg;
        }


    }
}
