package com.nxlh.manager.shiro;

import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.manager.model.dto.AdminDTO;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.service.AdminService;
import com.nxlh.manager.service.AuthorizeService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.apache.shiro.util.CollectionUtils;

import java.util.List;


/**
 * 自定义权限匹配和账号密码匹配
 */
public class MyRealm extends AuthorizingRealm {
    @Reference
    private AdminService adminService;

    @Reference
    private AuthorizeService authorizeService;


    //权限信息，包括角色以及权限
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        var userInfo = (AdminDTO) principals.getPrimaryPrincipal();
//        List<AuthorizeDTO> allAuthorizeByRole = authorizeService.getAllAuthorizeByRole(userInfo.getRoleid());
//        if (!CollectionUtils.isEmpty(allAuthorizeByRole)) {
//            authorizationInfo.addRole(userInfo.getRoleid());
//        }
//        authorizationInfo.addStringPermission(allAuthorizeByRole.get(0).getAuthorizename());

        return authorizationInfo;
    }


    /*主要是用来进行身份认证的，也就是说验证用户输入的账号和密码是否正确。*/
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        //获取用户的输入的账号.
        var uptoken = (UsernamePasswordToken) token.getPrincipal();
        //通过username从数据库中查找 User对象，如果找到，没找到.
        //实际项目中，这里可以根据实际情况做缓存，如果不做，Shiro自己也是有时间间隔机制，2分钟内不会重复执行该方法
        var userInfo = this.adminService.login(uptoken.getUsername(), new String(uptoken.getPassword()));
        if (userInfo == null || userInfo.getStatus() != 200) {
            return null;
        }
        var user = (AdminDTO) userInfo.getData();
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
                uptoken.getUsername(), //用户名
                new String(uptoken.getPassword()), //密码
                ByteSource.Util.bytes(user.getId()),//salt=username+salt
                getName()  //realm name

        );
        return authenticationInfo;
    }

}