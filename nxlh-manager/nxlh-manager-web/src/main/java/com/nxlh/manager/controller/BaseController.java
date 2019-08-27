package com.nxlh.manager.controller;

import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.exceptions.UnAuthorizeException;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.model.dto.AdminDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.vo.base.BaseQueryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Array;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class BaseController {

    @Value("${nxlh.pageSize}")
    protected int pageSize = 20;

    @Value("${nxlh.userRedisExprie}")
    private long userRedisExprie = 30L;

    protected final String[] defaultOrderBy={"addtime"};

    @Autowired
    @Lazy
    protected RedisCommand redisService;


    protected HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        return request;
    }
    /**
     * 请求头中的身份标识
     *
     * @return
     */
    protected String getToken() {
        return this.getRequest().getHeader("Authorization");
    }


    protected AdminDTO getUserInfo() throws UnAuthorizeException {
        var token = this.getToken();
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        var cache = this.redisService.get(token);
        if (cache == null) {
            throw new UnAuthorizeException("Redis未找到用户信息,请重新登录");
        }
        //重新续30天
        this.redisService.setExpireTime(token, userRedisExprie, TimeUnit.DAYS);
        return (AdminDTO) cache;

    }


    public PageParameter makePage(Optional vo) {
        if (vo.isPresent()) {
            return new PageParameter(((BaseQueryVO) vo.get()).getPageIndex()  , this.pageSize);
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


}
