package com.nxlh.manager.model.enums;


/**
 * 回收工单状态
 */
public enum RecycleEnums {

    Talk(0, "询价中"),
    Send(1, "待寄件"),
    Audit(2, "审核中"),
    Success(3, "交易成功"),
    Close(4, "交易结束");

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

    RecycleEnums(int val, String msg) {
        this.value = val;
        this.message = msg;
    }
}



