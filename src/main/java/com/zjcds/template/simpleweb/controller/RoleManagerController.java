package com.zjcds.template.simpleweb.controller;

import com.zjcds.common.dozer.BeanPropertyCopyUtils;
import com.zjcds.common.jsonview.annotations.JsonFailureBindResult;
import com.zjcds.common.jsonview.annotations.JsonViewException;
import com.zjcds.common.jsonview.utils.ResponseResult;

import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.dto.RoleForm;
import com.zjcds.template.simpleweb.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * created date：2017-09-22
 * @author niezhegang
 */
@JsonViewException
@RestController
@RequestMapping("/roles")
@Api(description = "角色管理类操作")
public class RoleManagerController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    @ApiOperation(value ="查询所有角色信息，不包含角色关联的菜单项",produces = "application/json;charset=utf-8")
    public ResponseResult<List<RoleForm.Owner>> queryAllRoles(){
        return new ResponseResult<>(BeanPropertyCopyUtils.copy(userService.queryAllRoles(),RoleForm.Owner.class));
    }

    @PostMapping
    @ApiOperation(value ="添加一个角色,可包含菜单信息",produces = "application/json;charset=utf-8")
    @JsonFailureBindResult
    public ResponseResult<RoleForm.OwnerWithMenu> addRole(@Valid @RequestBody RoleForm.Add roleForm, BindingResult errorResult ){
        return new ResponseResult<>(BeanPropertyCopyUtils.copy(userService.addRole(roleForm),RoleForm.OwnerWithMenu.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value ="更新一个角色信息",produces = "application/json;charset=utf-8")
    @JsonFailureBindResult
    public ResponseResult<RoleForm.OwnerWithMenu> updateRole(@PathVariable Integer id,@Valid @RequestBody RoleForm.Update roleForm,BindingResult errorResult){
        Assert.notNull(id,"要更新的角色id不能为空！");
        return new ResponseResult<>(BeanPropertyCopyUtils.copy(userService.updateRole(id,roleForm),RoleForm.OwnerWithMenu.class));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value ="删除一个角色信息",produces = "application/json;charset=utf-8")
    public ResponseResult<Void> deleteRole(@PathVariable Integer id){
        userService.deleteRole(id);
        return new ResponseResult(null);
    }

    @GetMapping("/{id}/menus")
    @ApiOperation(value ="查询一个角色的关联的所有菜单项",produces = "application/json;charset=utf-8")
    public ResponseResult<List<MenuForm.Owner>> queryMenuFor(@PathVariable("id") Integer id){
        Assert.notNull(id,"查询的角色id不能为空！");
        return new ResponseResult<>(BeanPropertyCopyUtils.copy(userService.queryMenuFor(id),MenuForm.Owner.class));
    }

}
