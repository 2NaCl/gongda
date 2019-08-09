package com.fyj.gongda.model.domain.system.response;

import com.fyj.gongda.model.domain.system.Permission;
import com.fyj.gongda.model.domain.system.Role;
import com.fyj.gongda.model.domain.system.User;
import lombok.Getter;
import lombok.Setter;
import org.crazycake.shiro.AuthCachePrincipal;

import java.io.Serializable;
import java.util.*;

@Setter
@Getter
public class ProfileResult implements Serializable,AuthCachePrincipal {
    private String studentId;
    private String username;
    private String company;
    private String companyId;
    private String level;
    private Map<String,Object> roles = new HashMap<>();

    /**
     *
     * @param user
     */
    public ProfileResult(User user, List<Permission> list) {
        this.studentId = user.getStudentId();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.level = user.getLevel();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();

        for (Permission perm : list) {
            String code = perm.getCode();
            if(perm.getType() == 1) {
                menus.add(code);
            }else if(perm.getType() == 2) {
                points.add(code);
            }else {
                apis.add(code);
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }
    public ProfileResult(User user) {
        this.studentId = user.getStudentId();
        this.username = user.getUsername();
        this.company = user.getCompanyName();
        this.companyId = user.getCompanyId();
        this.level = user.getLevel();
        Set<Role> roles = user.getRoles();
        Set<String> menus = new HashSet<>();
        Set<String> points = new HashSet<>();
        Set<String> apis = new HashSet<>();
        for (Role role : roles) {
            Set<Permission> perms = role.getPermissions();
            for (Permission perm : perms) {
                String code = perm.getCode();
                if(perm.getType() == 1) {
                    menus.add(code);
                }else if(perm.getType() == 2) {
                    points.add(code);
                }else {
                    apis.add(code);
                }
            }
        }
        this.roles.put("menus",menus);
        this.roles.put("points",points);
        this.roles.put("apis",apis);
    }

    @Override
    public String getAuthCacheKey() {
        return null;
    }
}
