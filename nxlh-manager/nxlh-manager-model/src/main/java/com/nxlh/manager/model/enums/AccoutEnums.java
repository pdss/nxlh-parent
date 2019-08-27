package com.nxlh.manager.model.enums;

public class AccoutEnums {

    public enum AccoutTypeEnums {
        Alipay(1, "支付宝"),
        WeChat(2, "微信");


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

        AccoutTypeEnums(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

    }
}
