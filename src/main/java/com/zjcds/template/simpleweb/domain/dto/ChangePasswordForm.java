package com.zjcds.template.simpleweb.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

/**
 * 修改密码用表单
 * created date：2018-01-25
 * @author niezhegang
 */
@Getter
@Setter
@ApiModel(value = "changePassword",description = "密码修改表单对象")
public class ChangePasswordForm {
    @ApiModelProperty(value = "旧密码",required = true)
    @NotNull(message = "旧密码不能为空！")
    private String oldPassword;
    @ApiModelProperty(value = "新密码",required = true)
    @NotNull(message = "新密码不能为空！")
    private String newPassword;
}
