package com.nxlh.common.model;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxlh.common.enums.HttpResponseEnums;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@ToString
public class MessageModel implements Serializable {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer Ret;

    // 响应消息
    private String Msg;

    // 响应中的数据
    private Object Data;


    public static MessageModel build(Integer status, String Msg, Object Data) {
        return new MessageModel(status, Msg, Data);
    }

    public static MessageModel build(HttpResponseEnums resp, Object Data) {
        return new MessageModel(resp, Data);
    }

    public static MessageModel ok(Object Data) {
        return new MessageModel(Data);
    }
    public static MessageModel ok(Object Data,String msg) {
        return new MessageModel(200,msg,Data);
    }

    public static MessageModel ok() {
        return new MessageModel(null);
    }

    public static MessageModel error(String error) {
        return new MessageModel(HttpResponseEnums.InternalServerError, error);
    }

    public static MessageModel forb(String error) {
        return new MessageModel(HttpResponseEnums.Forbidden.getValue(), error,"");
    }

    public MessageModel() {

    }

    public MessageModel(HttpResponseEnums resp, Object Data) {
        this.Ret = resp.getValue();
        this.Msg = resp.getMessage();
        this.Data = Data;
    }

    public static MessageModel build(Integer status, String Msg) {
        return new MessageModel(status, Msg, null);
    }

    public MessageModel(Integer status, String Msg, Object Data) {
        this.Ret = status;
        this.Msg = Msg;
        this.Data = Data;
    }

    public MessageModel(Object Data) {
        this.Ret = HttpResponseEnums.Ok.getValue();
        this.Msg = HttpResponseEnums.Ok.getMessage();
        this.Data = Data;
    }

//    public Boolean isOK() {
//        return this.status == HttpResponseEnums.Ok.getValue();
//    }

    public Integer getStatus() {
        return Ret;
    }

    public void setStatus(Integer status) {
        this.Ret = status;
    }

    public String getMsg() {
        return Msg;
    }

    public void setMsg(String Msg) {
        this.Msg = Msg;
    }

    public Object getData() {
        return Data;
    }

    public void setData(Object Data) {
        this.Data = Data;
    }



}
