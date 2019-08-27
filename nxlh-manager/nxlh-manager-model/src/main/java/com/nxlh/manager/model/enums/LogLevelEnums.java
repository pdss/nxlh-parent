package com.nxlh.manager.model.enums;

public class LogLevelEnums {


    public enum Level {
        normal(1, "普通"),
        fata(4, "严重错误");

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

        Level(int val, String msg) {
            this.value = val;
            this.message = msg;
        }


    }
}
