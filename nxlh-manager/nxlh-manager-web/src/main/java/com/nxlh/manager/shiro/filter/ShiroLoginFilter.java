package com.nxlh.manager.shiro.filter;

import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.model.dto.AdminDTO;
import com.nxlh.manager.shiro.JWTToken;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
@Component
public class ShiroLoginFilter extends AccessControlFilter implements BaseFilter {


    @Autowired
    protected RedisCommand redisSerive;

    @Value("${nxlh.dev}")
    private Boolean isDev = true;


    //可以不带token的请求路径
    private String[] exceptsByNoToken = {
            "/api/web/admin/login",
            "/api/web/admin/logout",
            "/api/file/upload"
    };

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object o) throws Exception {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        try {
            var client_type = httpServletRequest.getHeader("Authorization");
            if (StringUtils.isEmpty(client_type)) {
                this.log.info("====================================NO TOKEN : " + ((HttpServletRequest) request).getContextPath() + "====================================");
                var count = Arrays.stream(exceptsByNoToken).filter(e -> ((HttpServletRequest) request).getRequestURI().contains(e)).count();
                if (count > 0) {
                    return true;
                }
                return false;
            }
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;

            return this.validWebRequest(httpServletRequest, httpServletResponse);
        } catch (Exception ex) {
            this.log.error("==================================Shiro登录验证异常：==================================");
            ex.printStackTrace();
        }
        return false;
    }


    /**
     * 表示当访问拒绝时是否已经处理了；如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可。
     * onAccessDenied是否执行取决于isAccessAllowed的值，如果返回true则onAccessDenied不会执行；如果返回false，执行onAccessDenied
     * 如果onAccessDenied也返回false，则直接返回，不会进入请求的方法（只有isAccessAllowed和onAccessDenied的情况下）
     */

    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        this.responseJson((HttpServletResponse) servletResponse, HttpResponseEnums.UnAuthorized);
        return false;
    }


    //验证Web
    private boolean validWebRequest(HttpServletRequest request, HttpServletResponse httpServletResponse) throws IOException {
        var token = request.getHeader("Authorization");


        if (StringUtil.isNullOrEmpty(token) || !this.redisSerive.exists(token)) {
            //this.responseJson(httpServletResponse, HttpResponseEnums.UnAuthorized);
            return false;

        } else {
           // var user = this.redisSerive.get(token);
            return true;
        }

    }
}
