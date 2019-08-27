package com.nxlh.minapp.shiro;

import com.nxlh.minapp.shiro.filter.LogOutFilter;
import com.nxlh.minapp.shiro.filter.ShiroLoginFilter;
import com.nxlh.minapp.shiro.filter.ShiroPermsFilter;
import com.nxlh.minapp.shiro.filter.ShiroRolesFilter;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,ShiroLoginFilter loginFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        // filters.put("authc",new ExtendFormAuthenticationFilter());
        //自定义的退出登录操作
//        filters.put("logout",new LogOutFilter());
        filters.put("authc",loginFilter);
//        filters.put("perms",new ShiroPermsFilter());
//        filters.put("roles",new ShiroRolesFilter());

        shiroFilterFactoryBean.setFilters(filters);
        //拦截器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //filterChainDefinitionMap.put("/**","authc");

        //登录页
        //shiroFilterFactoryBean.setLoginUrl("/api/web/account/login");

        //登录成功后跳转的页面
        // shiroFilterFactoryBean.setSuccessUrl("/home");

        //未授权页面
//        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    @Bean
    public MyRealm myShiroRealm() {
        MyRealm myShiroRealm = new MyRealm();
        return myShiroRealm;
    }


    @Bean("securityManager")
    public DefaultWebSecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myShiroRealm());
        return securityManager;
    }

    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题
        // https://zhuanlan.zhihu.com/p/29161098
        defaultAdvisorAutoProxyCreator.setProxyTargetClass(true);
        return defaultAdvisorAutoProxyCreator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }
}