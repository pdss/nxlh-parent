package com.nxlh.minapp.shiro;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

public class MyRealm extends AuthorizingRealm {



    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JWTToken;
    }

    /**
     * 验证
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        String username = JWTUtils.getAdminname(principals.toString());
//        AdminDTO user = userService(username);
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
//        simpleAuthorizationInfo.addRole(user.getRole());
//        Set<String> permission = new HashSet<>(Arrays.asList(user.getPermission().split(",")));
//        simpleAuthorizationInfo.addStringPermissions(permission);
        return simpleAuthorizationInfo;
    }

    /**
     * 登录
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
//        String username = JWTUtils.getAdminname(token);
//        if (username == null) {
//            throw new AuthenticationException("token invalid");
//        }

//        AdminDTO userDTO = userService.getByAdminName(username);
//        if (userDTO == null) {
//            throw new AuthenticationException("用户不存在");
//        }
//
//        if (!JWTUtils.verify(token, username, userDTO.getLoginpassword())) {
//            throw new AuthenticationException("用户名或密码不对");
//        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}