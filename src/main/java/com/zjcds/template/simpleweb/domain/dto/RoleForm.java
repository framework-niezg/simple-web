package com.zjcds.template.simpleweb.domain.dto;

import com.zjcds.common.base.domain.BaseBean;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * created date：2017-09-19
 * @author niezhegang
 */
public class RoleForm {

    @Getter
    @Setter
    @ApiModel(value = "role",description = "角色对象")
    public static class Owner extends BaseBean{
        @ApiModelProperty(value = "角色id",required = true,readOnly = true,example = "0")
        private Integer id;
        @ApiModelProperty(value = "角色名称：最大30个字符",required = true,readOnly = true,example = "root")
        private String name;
        @ApiModelProperty(value = "角色描述：最大100个字符",required = false,example = "超级管理员角色")
        private String desc;
        @ApiModelProperty(value = "创建时间",dataType = "long",example = "24424255252")
        private Date createTime;
        @ApiModelProperty(value = "修改时间",dataType = "long",example = "24424255252")
        private Date modifyTime;
    }

    @Getter
    @Setter
    @ApiModel(value = "role.add",description = "添加角色表单对象")
    public static class Add extends BaseBean{
        @ApiModelProperty(value = "角色名称：最大30个字符",required = true,readOnly = true,example = "root")
        @NotNull
        @Size(max=30)
        private String name;
        @ApiModelProperty(value = "角色描述：最大100个字符",required = false,example = "超级管理员角色")
        private String desc;
        @Valid
        private Set<MenuForm.Reference> menus = new HashSet<>();
    }

    @Getter
    @Setter
    @ApiModel(value = "role.update",description = "修改角色表单对象")
    public static class Update extends BaseBean{
        @ApiModelProperty(value = "角色描述：最大100个字符",required = false,example = "超级管理员角色")
        private String desc;
        @Valid
        private Set<MenuForm.Reference> menus = new HashSet<>();
    }

    @Getter
    @Setter
    @ApiModel(value = "roleWithMenu",description = "带菜单的角色对象")
    public static class OwnerWithMenu extends Owner{
        private Set<MenuForm> menus = new HashSet<>();
    }

    @Getter
    @Setter
    @ApiModel(value = "roleReference",description = "角色关联使用对象--关联角色id")
    public static class Reference{
        @ApiModelProperty(value = "角色id",required = true,readOnly = true,example = "0")
        @NotNull
        private Integer id;
    }
}
