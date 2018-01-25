package com.zjcds.template.simpleweb.service;

import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.jpa.PageResult;
import com.zjcds.template.simpleweb.domain.dto.ChangePasswordForm;
import com.zjcds.template.simpleweb.domain.dto.RoleForm;
import com.zjcds.template.simpleweb.domain.dto.UserForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;


import java.util.List;
import java.util.Set;

/**
 * created date：2017-02-08
 * @author niezhegang
 */
public interface UserService {

    /**
     * 根据用户id查询用户
     * @param id
     * @return
     */
    public User queryUserWithRole(Integer id);

    /**
     * 通过用户名查询用户实体
     * 未查到返回null
     * @param accunt
     * @return
     */
    public User queryUserByName(String accunt);

    /**
     * 根据传入的分页参数查询
     * @param paging
     * @return
     */
    public PageResult<User> queryUsers(Paging paging, List<String> queryString, List<String> orderBy);

    /**
     * 查询出系统所有的菜单表单项
     * @return
     */
    public Set<Menu> queryAllMenus();

    /**
     * 查询出用户所有菜单项
     * @param id
     * @return
     */
    public Set<Menu> queryAllMenusForUser(Integer id);

    /**
     * 查询当前登录用户菜单，
     * root角色用户查询出所有菜单
     * @return
     */
    public Set<Menu> queryMenusForCurrentUser();

    /**
     * 增加一个用户
     * @param user
     * @return
     */
    public User addUser(UserForm.Add user);

    /**
     * 修改用户信息
     * @param id
     * @param user
     * @return
     */
    public User updateUser(Integer id, UserForm.Update user);

    /**
     * 删除一个用户
     * @param id
     */
    public void deleteUser(Integer id);

    /**
     * 删除一个角色
     * 已有用户关联的角色不允许删除
     * @param id
     */
    public void deleteRole(Integer id);

    /**
     * 修改角色信息
     * @param id
     * @param roleForm
     * @return
     */
    public Role updateRole(Integer id, RoleForm.Update roleForm);

    /**
     * 增加一个新角色
     * 角色名不能相同
     * @param roleForm
     * @return
     */
    public Role addRole(RoleForm.Add roleForm);

    /**
     * 查询所有角色
     * @return
     */
    public List<Role> queryAllRoles();

    /**
     * 查询角色所有关联菜单项
     * @param roleId
     * @return
     */
    public List<Menu> queryMenuFor(Integer roleId);

    /**
     * 修改用户密码
     * @param changePasswordForm
     */
    public void changePassword(ChangePasswordForm changePasswordForm);

}
