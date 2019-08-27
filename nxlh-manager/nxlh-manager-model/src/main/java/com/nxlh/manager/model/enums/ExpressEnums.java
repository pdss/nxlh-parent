package com.nxlh.manager.model.enums;

public class ExpressEnums {

    /**
     * 快递
     */
    public enum Express {
        zto(0, "中通快递"),
        sf(1, "顺丰快递");


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

        Express(int val, String msg) {
            this.value = val;
            this.message = msg;
        }

        public static Express from(int val) {
            for (var exp : Express.values()) {
                if (exp.getValue() == val) {
                    return exp;
                }
            }
            return null;
        }

    }
}
