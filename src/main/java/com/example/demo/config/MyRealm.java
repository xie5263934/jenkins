package com.example.demo.config;

import com.example.demo.entity.SysPermission;
import com.example.demo.entity.SysRole;
import com.example.demo.entity.UserInfo;
import com.example.demo.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 实现自己的REALM，继承AuthorinsingReamlm
 * 主要重写doGetAuthenticationInfo和doGetAuthorizationInfo这两个方法
 * 其中doGetAuthenticationInfo方法主要是用来验证用户的身份，然后返回SimpleAuthenticationInfo
 * doGetAuthorizationInfo方法主要用来获取用户的角色和权限信息，然后返回给shiro，配合filter中定义的规则和@requiredPermission注解要求的权限检查，该方法只获取用户的角色和权限，真正的校验还是由shiro去校验的。我们不实现校验逻辑
 */
public class MyRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("权限配置-->MyShiroRealm.doGetAuthorizationInfo()");
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        UserInfo userInfo = (UserInfo) principalCollection.getPrimaryPrincipal();
        for (SysRole role : userInfo.getRoleList()) {
            info.addRole(role.getRole());
            for (SysPermission permission : role.getPermissionList()) {
                info.addStringPermission(permission.getPermission());
            }
        }

        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("MyShiroRealm.doGetAuthenticationInfo()");
        String userName = (String) authenticationToken.getPrincipal();
        System.out.println(authenticationToken.getCredentials());
        UserInfo userInfo = userService.findByUserName(userName);
        System.out.println("----->>userInfo=" + userInfo);
        if (userInfo == null) {
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(userInfo, userInfo.getPassWord(), ByteSource.Util.bytes(userInfo.getSalt()), getName());
        return info;
    }
}
