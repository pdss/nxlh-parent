package com.nxlh.minapp.controller;


import com.github.binarywang.java.emoji.EmojiConverter;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.MyException;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.vo.base.BaseQueryVO;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BaseController {

    protected  EmojiConverter emojiConverter = EmojiConverter.getInstance();

    @Value("${nxlh.pageSize}")
    protected int pageSize = 20;

    @Autowired
    protected Mapper beanMapper;

    @Value("${nxlh.userRedisExprie}")
    private long userRedisExprie = 20L;

    @Autowired
    @Lazy
    protected RedisCommand redisService;

    protected <T> T beanMap(Object source, Class<T> targetClazz) {
        if (source == null) {
            return null;
        }
        return this.beanMapper.map(source, targetClazz);
    }

    protected <T> List<T> beanListMap(List object, Class<T> targetClazz) {
        if (CollectionUtils.isEmpty(object)) {
            return null;
        }
        var beans = new ArrayList<T>();
        object.forEach(e -> {
            beans.add(this.beanMapper.map(e, targetClazz));
        });
        return beans;
    }

    /**
     * 请求头中的身份标识
     *
     * @return
     */
    protected String getToken() {
        return this.getRequest().getHeader("Authorization");
    }

    /**
     * 获取当前用户的ID
     *
     * @return
     */
    protected String getUserId() throws UnAuthorizeException {
        var token = this.getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        var cache = this.redisService.get(token);
        if (cache == null) {
            throw new UnAuthorizeException("Redis未找到用户信息,请重新登录");
        }
        this.refreshUserExpire();
        var userInfo = (WxUserDTO) cache;
        return userInfo.getId();
    }

    protected WxUserDTO getUserInfo() throws UnAuthorizeException {
        var token = this.getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        var cache = this.redisService.get(token);
        if (cache == null) {
            throw new UnAuthorizeException("Redis未找到用户信息,请重新登录");
        }
        this.refreshUserExpire();
        var user = (WxUserDTO) cache;
        user.setNickname(this.emojiConverter.toUnicode(user.getNickname()));
        return user;
    }

    /**
     * 刷新用户的有效期
     */
    protected void refreshUserExpire() {
        //重新续20天
        this.redisService.setExpireTime(this.getToken(), userRedisExprie, TimeUnit.DAYS);
    }

    protected final String[] defaultOrderBy = {"addtime"};


    protected HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }


    public PageParameter makePage(Optional vo) {
        if (vo.isPresent()) {
            return new PageParameter(((BaseQueryVO) vo.get()).getPageIndex() - 1, this.pageSize);
        }
        return new PageParameter(0, this.pageSize);
    }

    public PageParameter makePage(BaseQueryVO vo) {

        return new PageParameter(vo.getPageIndex(), this.pageSize);
    }


    protected MyResult json(int ret, String msg, Object data) {
        return new MyResult(ret, msg, data);
    }

    protected MyResult json(HttpResponseEnums res, Object data) {
        return new MyResult(res, data);
    }

    public MyResult ok(Object data) {
        return new MyResult(HttpResponseEnums.Ok, data);
    }

    public MyResult unauth(Object data) {
        return new MyResult(HttpResponseEnums.UnAuthorized, data);
    }

    public MyResult badreq(Object data) {
        return new MyResult(HttpResponseEnums.BadRequest, data);
    }

    public MyResult forb(Object data) {
        return new MyResult(HttpResponseEnums.Forbidden, data);
    }


}
