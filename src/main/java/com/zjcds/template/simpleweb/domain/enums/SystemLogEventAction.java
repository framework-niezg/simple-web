package com.zjcds.template.simpleweb.domain.enums;


import com.zjcds.template.simpleweb.domain.entity.Role;
import com.zjcds.template.simpleweb.domain.entity.User;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import java.util.Set;

/**
 * 系统日志事件行为
 * created date：2017-09-25
 * @author niezhegang
 */
@Getter
public enum SystemLogEventAction {

    UserAdd("userAdd","添加用户账号"){
        @Override
        public String getSystemLogEventDetail(Object source) {
            if(source!= null && source instanceof User){
                return super.userDetail((User) source);
            }
            else {
                return null;
            }
        }
    },
    UserUpdate("userUpdate","修改用户账号信息"){
        @Override
        public String getSystemLogEventDetail(Object source) {
            if(source!= null && source instanceof User){
                return super.userDetail((User) source);
            }
            else {
                return null;
            }
        }
    },
    UserDelete("userDelete","删除用户账号"){
        @Override
        public String getSystemLogEventDetail(Object source) {
            if(source!= null && source instanceof User){
                return super.userDetail((User) source);
            }
            else {
                return null;
            }
        }
    },

    RoleAdd("roleAdd","添加角色"),
    RoleUpdate("roleUpdate","修改角色信息"),
    RoleDelete("roleDelete","删除角色");

    SystemLogEventAction(String action,String desc){
        this.name = action;
        this.desc = desc;
    }

    public String getSystemLogEventDetail(Object source){
        return desc;
    }

    private String userDetail(User user){
        StringBuilder sb = new StringBuilder();
        sb.append(getDesc())
                .append("，账号信息为：{")
                .append("id=").append(user.getId()).append(",")
                .append("账号=").append(user.getAccount()).append(",")
                .append("显示名=").append(user.getName()).append(",")
                .append("状态=").append(user.getStatus()).append(",");
        Set<Role> roles = user.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            int i = 0;
            for(Role role : roles){
                if(i == 0){
                    sb.append("分配角色=[");
                }
                else {
                    sb.append(",");
                }
                sb.append(role.getName());
                i++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
    }

    private String name;

    private String desc;
}
