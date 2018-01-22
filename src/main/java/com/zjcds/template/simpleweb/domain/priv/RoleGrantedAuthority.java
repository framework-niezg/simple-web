package com.zjcds.template.simpleweb.domain.priv;

import com.zjcds.common.base.domain.BaseBean;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * created date：2017-09-12
 * @author niezhegang
 */
public class RoleGrantedAuthority extends BaseBean implements GrantedAuthority{

    private final String role;

    private final String roleAuthrity;

    private final static String DefaultRolePrefix = "ROLE_";

    public RoleGrantedAuthority(String role) {
        Assert.hasText(role, "角色不能为空！");
        this.role = role;
        if(StringUtils.startsWith(role,DefaultRolePrefix))
            this.roleAuthrity = role;
        else
            this.roleAuthrity = DefaultRolePrefix+role;
    }

    public String getAuthority() {
        return roleAuthrity;
    }

    /**
     * 判定角色相等
     * @param role
     * @return
     */
    public boolean equalRole(String role) {
        if(StringUtils.equals(this.role,role) || StringUtils.equals(this.roleAuthrity,role))
            return true;
        else
            return false;
    }
}
