package com.nxlh.minapp.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.annotation.ApiController;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MessageModel;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.service.WxUserService;
import com.nxlh.minapp.model.BindPhoneVO;
import com.nxlh.minapp.model.order.WxUserRegisterVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@ApiController("/api/wx/user")
public class UserController extends BaseController {

    @Reference
    private WxUserService wxUserService;

    @Autowired
    private RedisCommand redisService;

    /**
     * 微信端Code=>SessionKey
     *
     * @return
     */
    @GetMapping("login")
    public MyResult login(@RequestParam String code) {
        if (StringUtils.isEmpty(code)) {
            return this.badreq(null);
        }

        return this.wxUserService.login(code);
    }

    /**
     * 用户登录状态
     * *
     */
    @GetMapping("getstatus")
    public MyResult isLogin() throws Exception {

        var user = this.getUserInfo();
        user.setSessionKey(null);
        user.setOpenid(null);
        user.setId(null);
        return MyResult.ok(user);
    }


    /**
     * 绑定微信用户信息
     *
     * @return
     */
    @PostMapping("register")
    public MyResult registerWxUserInfo(@RequestBody WxUserRegisterVO req) throws Exception {
        var params = ObjectUtils.objectToMap(req);
        params.put("userid", this.getUserId());
        params.put("sessionkey",this.getUserInfo().getSessionKey());
        return this.wxUserService.bindWxUser(params);

    }


    /**
     * 获取积分
     *
     * @return
     */
    @GetMapping("getscore")
    public MyResult getScore() throws UnAuthorizeException {
        var userInfo = this.wxUserService.getById(this.getUserId());
        return MyResult.ok(new HashMap<String, BigDecimal>() {{
            put("score", userInfo.getVscore());
            put("sumpay", userInfo.getSumpay());
        }});
    }

    @PostMapping("bindwxphone")
    public MyResult bindWxPhone(@RequestBody BindPhoneVO values) throws UnAuthorizeException {
        var sessionKey = this.getUserInfo().getSessionKey();
        return this.wxUserService.bindWxPhone(sessionKey, values.getEncryptedData(), values.getIv(), this.getUserId());
    }


    @PostMapping("syncuser")
    public MessageModel syncuser(@RequestBody WxUserRegisterVO model) throws IllegalAccessException {
        var params = ObjectUtils.objectToMap(model);
        return this.wxUserService.syncUser(params);

    }

}
