package com.zjcds.template.simpleweb.domain.dto;

import com.zjcds.common.base.domain.BaseBean;
import com.zjcds.template.simpleweb.domain.entity.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * created date：2017-08-31
 * @author niezhegang
 */

public class UserForm extends BaseBean {

    @Getter
    @Setter
    @ApiModel(value = "user",description = "用户对象")
    public static class Owner extends BaseBean{
        @ApiModelProperty(value = "用户ID",required = true,readOnly = true)
        private Integer id;
        @NotNull
        @ApiModelProperty(value = "用户账号：最大30个字符",required = true,readOnly = true,example = "admin")
        private String account;
        @ApiModelProperty(value = "用户显示名：最大50个字符",required = true,example = "admin")
        private String name;
        @NotNull
        @ApiModelProperty(value = "用户密码：不为空，限制最大20个字符",required = true,example = "password")
        private String password;
        @ApiModelProperty(value = "账号状态：active为可用状态",required = true,example = "active")
        private User.UserStatus status;
        @ApiModelProperty(value = "创建时间",dataType = "long",example = "24424255252")
        private Date createTime;
        @ApiModelProperty(value = "修改时间",dataType = "long",example = "24424255252")
        private Date modifyTime;
    }

    @Getter
    @Setter
    @ApiModel(value = "userAdd",description = "新建用户表单对象")
    public static class Add extends BaseBean{
        @Size(min=1,max = 30)
        @NotNull
        @ApiModelProperty(value = "用户账号：最大30个字符",required = true,readOnly = true,example = "admin")
        private String account;
        @ApiModelProperty(value = "用户显示名：最大50个字符",required = true,example = "admin")
        @Size(min=1,max = 50)
        @NotNull
        private String name;
        @Size(min=1,max = 20)
        @NotNull
        @ApiModelProperty(value = "用户密码：不为空，限制最大20个字符",required = true,example = "password")
        private String password;
        @Valid
        private Set<RoleForm.Reference> roles;
    }

    @Getter
    @Setter
    @ApiModel(value = "userUpdate",description = "修改用户表单对象")
    public static class Update extends BaseBean{
        @ApiModelProperty(value = "用户显示名：最大50个字符",required = true,example = "admin")
        @Size(min=1,max = 50)
        @NotNull
        private String name;
        @Size(min=1,max = 255)
        @NotNull
        @ApiModelProperty(value = "用户密码：不为空，限制最大255个字符",required = true,example = "password")
        private String password;
        @Valid
        private Set<RoleForm.Reference> roles;
    }

    @Getter
    @Setter
    @ApiModel(value = "userWithRole",description = "用户对象")
    public static class OwnerWithRole extends Owner{
        private Set<RoleForm.Owner> roles;
    }

}
