package com.nxlh.common.enums;

public enum HttpResponseEnums {

    Ok(200, "Ok"),
    UnAuthorized(401, "未登录"),
    InternalServerError(500, "系统异常"),
    BadRequest(400, "请求参数无效"),
    Forbidden(403, "无权限");

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

    private HttpResponseEnums(int val) {
        this.value = val;
    }

    private HttpResponseEnums(int val, String msg) {
        this.value = val;
        this.message = msg;
    }


}