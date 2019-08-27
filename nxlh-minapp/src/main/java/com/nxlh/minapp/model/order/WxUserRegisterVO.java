package com.nxlh.minapp.model.order;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class WxUserRegisterVO extends BaseVO {
    @NotBlank
    private String avatarurl;
    @NotBlank
    private String city;
    @NotBlank
    private String country;
    private int gender;
    @NotBlank
    private String nickname;
    @NotBlank
    private String province;

    private String  encrypteddata;

    private String iv;

    private String unionid;

    private String headimgurl;

    private Integer sex;
}
