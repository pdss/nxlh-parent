package com.nxlh.manager.utils;

import com.aliyuncs.CommonRequest;
import com.aliyuncs.http.MethodType;

public class NxlhCommonRequest<T> extends CommonRequest {
    public NxlhCommonRequest() {
        super();
        this.changValue();
    }

    public NxlhCommonRequest(T TemplateCode) {
        super();
        this.changValue();
        this.putQueryParameter("TemplateCode", TemplateCode.toString());
    }

    private void changValue() {
        this.setMethod(MethodType.POST);
        this.setDomain("dysmsapi.aliyuncs.com");
        this.setVersion("2017-05-25");
        this.setAction("SendSms");
        this.putQueryParameter("RegionId", "cn-hangzhou");
        //必填:短信签名-可在短信控制台中找到
        this.putQueryParameter("SignName", "宁心力合数码");
    }

}
