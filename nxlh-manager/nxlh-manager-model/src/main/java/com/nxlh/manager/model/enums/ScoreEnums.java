package com.nxlh.manager.model.enums;

public class ScoreEnums {


    /**
     * 积分类型
     */
    public enum ScoreTypeEnums {

        minapp(1, "小程序"),
        other(2, "其他平台");

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

        ScoreTypeEnums(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }


}