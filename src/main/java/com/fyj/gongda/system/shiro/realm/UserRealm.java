package com.fyj.gongda.system.shiro.realm;

import com.fyj.gongda.common.shiro.realm.IhrmRealm;
import com.fyj.gongda.model.domain.system.Permission;
import com.fyj.gongda.model.domain.system.User;
import com.fyj.gongda.model.domain.system.response.ProfileResult;
import com.fyj.gongda.system.service.PermissionService;
import com.fyj.gongda.system.service.UserService;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRealm extends IhrmRealm {

    @Autowired
    private UserService userService;

    @Autowired
    private PermissionService permissionService;

    //认证方法
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //1.获取用户的学号和密码
        UsernamePasswordToken upToken = (UsernamePasswordToken) authenticationToken;
        String studentId = upToken.getUsername();
        String password = new String(upToken.getPassword());
//        password = new Md5Hash(password, studentId, 3).toString();
        System.out.println(password);
        //2.根据学号查询用户
        User user = userService.findByMobile(studentId);
        System.out.println(user.getPassword());
        //3.判断用户yesno存在，用户密码yesno和输入密码一致
        if(user != null && user.getPassword().equals(password)) {
            //4.构造安全数据并返回（安全数据：用户基本数据，权限信息 profileResult）
            ProfileResult result = null;
            if("user".equals(user.getLevel())) {
                result = new ProfileResult(user);
            }else {
                Map map = new HashMap();
                if("coAdmin".equals(user.getLevel())) {
                    map.put("enVisible","1");
                }
                List<Permission> list = permissionService.findAll(map);
                result = new ProfileResult(user,list);
            }
            //构造方法：安全数据，密码，realm域名
            SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(result,user.getPassword(),this.getName());
            return info;
        }
        //返回null，会抛出异常，标识用户名和密码不匹配
        return null;
    }
}
