package com.nxlh.manager.shiro;

import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.shiro.MyRealm;
import com.nxlh.manager.shiro.filter.LogOutFilter;
import com.nxlh.manager.shiro.filter.ShiroLoginFilter;
import com.nxlh.manager.shiro.filter.ShiroPermsFilter;
import com.nxlh.manager.shiro.filter.ShiroRolesFilter;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.session.mgt.eis.JavaUuidSessionIdGenerator;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.core.env.Environment;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//@Configuration
public class ShiroConfig {


    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager, ShiroLoginFilter loginFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        Map<String, Filter> filters = new HashMap<>();
        //自定义的退出登录操作
        filters.put("logout", new LogOutFilter());
        filters.put("myauth", loginFilter);
//        filters.put("perms",new ShiroPermsFilter());
        filters.put("roles", new ShiroRolesFilter());

        shiroFilterFactoryBean.setFilters(filters);
        //拦截器链
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/**", "myauth");


        //登录页
        shiroFilterFactoryBean.setLoginUrl("/api/web/admin/unauth");

        //登录成功后跳转的页面
        // shiroFilterFactoryBean.setSuccessUrl("/home");

        //未授权页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/api/web/admin/unauth");
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



    /**
     * 开启shiro aop注解支持.
     * 使用代理方式;所以需要开启代码支持;
     *
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

}