package com.zjcds.template.simpleweb.controller;

import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.dozer.BeanPropertyCopyUtils;
import com.zjcds.common.jpa.PageResult;
import com.zjcds.common.jpa.utils.PageUtils;
import com.zjcds.common.jsonview.annotations.JsonFailureBindResult;
import com.zjcds.common.jsonview.annotations.JsonViewException;
import com.zjcds.common.jsonview.utils.ResponseResult;

import com.zjcds.template.simpleweb.domain.dto.ChangePasswordForm;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.dto.UserForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.dao.jpa.um.RoleDao;
import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.MenuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * created date：2017-08-31
 * @author niezhegang
 */
@RestController
@RequestMapping("/users")
@Api(description = "用户管理类操作")
@JsonViewException
public class UserManagerController {

    @Autowired
    private UserService userService;
    @Autowired
    private RoleDao roleDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping
    @ApiOperation(value ="批量查询user操作，支持分页查询",produces = "application/json;charset=utf-8")
    @ApiImplicitParams({@ApiImplicitParam(name = "pageIndex" ,value = "分页页码" ,defaultValue = "1",dataType = "int",paramType = "query"),
                        @ApiImplicitParam(name = "limit" ,value = "返回行数",defaultValue = "2147483647",dataType = "int",paramType = "query"),
                        @ApiImplicitParam(name = "queryString" ,value = "查询条件",defaultValue = "field~Eq~1234",dataType = "String",paramType = "query",allowMultiple = true),
                        @ApiImplicitParam(name = "orderBy" ,value = "排序",defaultValue = "field1Desc",dataType = "String",paramType = "query",allowMultiple = true)})
    public ResponseResult<PageResult<UserForm.OwnerWithRole>> queryUsers(Paging paging,
                                                                         @RequestParam(required = false,name = "queryString") List<String> queryString,
                                                                         @RequestParam(required = false,name = "orderBy") List<String> orderBys) {
        if(queryString == null){
            queryString = new ArrayList<>();
        }
        //只查询活动用户
        queryString.add("status~Eq~active");
        PageResult<User> pageResult = userService.queryUsers(paging,queryString,orderBys);
        return new ResponseResult(PageUtils.copyPageResult(pageResult, UserForm.OwnerWithRole.class));
    }


    @GetMapping("/{id}")
    @ApiOperation(value ="根据id查询单个user对象",produces = "application/json;charset=utf-8")
    public ResponseResult<UserForm.OwnerWithRole> queryUser(@PathVariable("id") Integer id) throws Exception{
        User user = userService.queryUserWithRole(id);
        return new ResponseResult(BeanPropertyCopyUtils.copy(user,UserForm.OwnerWithRole.class));
    }

    @PostMapping
    @ApiOperation(value ="添加一个用户,可包含角色信息",produces = "application/json;charset=utf-8")
    @JsonFailureBindResult
    public ResponseResult<UserForm.OwnerWithRole> addUser(@Valid @RequestBody UserForm.Add user,BindingResult errorResult ){
        return new ResponseResult(BeanPropertyCopyUtils.copy(userService.addUser(user),UserForm.OwnerWithRole.class));
    }

    @PutMapping("/{id}")
    @ApiOperation(value ="更新一个用户信息",produces = "application/json;charset=utf-8")
    @JsonFailureBindResult
    public ResponseResult<UserForm.OwnerWithRole> updateUser(@PathVariable("id") Integer id,@Valid @RequestBody UserForm.Update user,BindingResult errorResult){
        Assert.notNull(id,"要更新的用户id不能为空！");
        return new ResponseResult(BeanPropertyCopyUtils.copy(userService.updateUser(id,user),UserForm.OwnerWithRole.class));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value ="删除一个用户信息",produces = "application/json;charset=utf-8")
    public ResponseResult<Void> deleteUser(@PathVariable("id") Integer id){
        userService.deleteUser(id);
        return new ResponseResult(null);
    }

    @PutMapping("/{id}/resetPassword")
    @ApiOperation(value ="用户密码重置",produces = "application/json;charset=utf-8")
    public ResponseResult<Void> resetUserPassword(@PathVariable("id") Integer id){
        String newPassword = userService.resetUserPassword(id);
        return new ResponseResult(newPassword);
    }

    @GetMapping("/current/menus")
    @ApiOperation(value ="查询当前登录用户的菜单项",produces = "application/json;charset=utf-8")
    public ResponseResult<List<MenuForm>> queryCurrentUserMenu(){
           return new ResponseResult(MenuUtils.transfromToMenuForm(userService.queryMenusForCurrentUser()));
    }

    @PutMapping("/current/change/password")
    @ApiOperation(value ="修改当前用户密码",produces = "application/json;charset=utf-8")
    public ResponseResult<Void> changePassword(@Valid @RequestBody ChangePasswordForm changePasswordForm,BindingResult errorResult){
        userService.changePassword(changePasswordForm);
        return new ResponseResult(null);
    }

}
