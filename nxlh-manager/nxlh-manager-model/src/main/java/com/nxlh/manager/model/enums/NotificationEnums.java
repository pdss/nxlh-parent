package com.nxlh.manager.model.enums;

public class NotificationEnums {

    /**
     * 通知类型
     */
    public enum NotificationTypeEnums {

        system(1, "系统通知"),
        user(2, "用户");

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

        NotificationTypeEnums(int val, String msg) {
            this.value = val;
            this.message = msg;
        }
    }



}
