package com.nxlh.manager.model.enums;

public class VIPEnums {

    /**
     * 会员类型
     */
    public enum VIPTypeEnum {
        None(0, "普通用户"),
        Gold(3, "金牌"),
        Silver(2, "银牌"),
        Bornze(1, "铜牌");


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

        VIPTypeEnum(int value, String msg) {
            this.value = value;
            this.message = msg;
        }


    }

}
