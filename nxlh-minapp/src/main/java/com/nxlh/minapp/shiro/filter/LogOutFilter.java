package com.nxlh.minapp.shiro.filter;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.LogoutFilter;

import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * 自定义的Shiro退出登录
 */

public class LogOutFilter extends LogoutFilter {
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //在这里执行退出系统前需要清空的数据
        Subject subject = getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        ServletContext context = request.getServletContext();
        try {
            subject.logout();
            context.removeAttribute("error");
        } catch (SessionException e) {
            e.printStackTrace();
        }
        issueRedirect(request, response, redirectUrl);
        //返回false表示不执行后续的过滤器，直接返回跳转到登录页面
        //这里返回true,因为Controller中需要手动完成一些善后操作
        return true;
    }
}