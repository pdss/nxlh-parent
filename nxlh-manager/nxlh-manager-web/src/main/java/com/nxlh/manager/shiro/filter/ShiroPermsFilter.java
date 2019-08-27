package com.nxlh.manager.shiro.filter;

import com.nxlh.common.enums.HttpResponseEnums;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ShiroPermsFilter extends PermissionsAuthorizationFilter implements BaseFilter {
    /**
     * shiro认证perms资源失败后回调方法
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws IOException
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws IOException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;
        this.responseJson(httpServletResponse, HttpResponseEnums.Forbidden);
        return false;
    }
}