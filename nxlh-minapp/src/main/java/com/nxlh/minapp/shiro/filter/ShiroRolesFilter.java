package com.nxlh.minapp.shiro.filter;

import com.nxlh.common.enums.HttpResponseEnums;
import org.apache.shiro.web.filter.authz.RolesAuthorizationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

public class ShiroRolesFilter extends RolesAuthorizationFilter implements BaseFilter {

    //在非法用户角色时调用
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
//        return super.onAccessDenied(request, response, mappedValue);

        //获取被要求的角色
        String[] rolesArray = (String[]) mappedValue;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        this.responseJson(httpServletResponse, HttpResponseEnums.Forbidden);

        return false;
    }
}