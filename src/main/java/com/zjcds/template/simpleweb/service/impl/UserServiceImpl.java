package com.zjcds.template.simpleweb.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.dozer.DozerConfiguration;
import com.zjcds.common.jpa.PageResult;

import com.zjcds.template.simpleweb.domain.dto.ChangePasswordForm;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.dto.RoleForm;
import com.zjcds.template.simpleweb.domain.dto.UserForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.domain.event.UserAddEvent;
import com.zjcds.template.simpleweb.domain.event.UserDeleteEvent;
import com.zjcds.template.simpleweb.domain.event.UserUpdateEvent;
import com.zjcds.template.simpleweb.dao.jpa.um.MenuDao;
import com.zjcds.template.simpleweb.dao.jpa.um.RoleDao;
import com.zjcds.template.simpleweb.dao.jpa.um.UserDao;
import com.zjcds.template.simpleweb.service.SystemEventPublishService;
import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * created date：2017-08-29
 * @author niezhegang
 */
@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SystemEventPublishService systemEventPublishService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public User queryUserWithRole(Integer id) {
        return userDao.findById(id);
    }

    @Override
    public User queryUserByName(String accunt) {
        return userDao.findByAccount(accunt);
    }

    @Override
    public PageResult<User> queryUsers(Paging paging,List<String> queryString,List<String> orderBy) {
        PageResult<User> userPage = userDao.findAllForFetchRole(paging,queryString,orderBy);
        return userPage;
    }

    @Override
    public Set<Menu> queryAllMenus() {
        return menuDao.queryAllMenu();
    }

    @Override
    public Set<Menu> queryAllMenusForUser(Integer id) {
        return userDao.findMenusForUser(id);
    }

    @Override
    public Set<Menu> queryMenusForCurrentUser() {
        if(!WebSecurityUtils.isLogined())
            return Collections.emptySet();
        if(WebSecurityUtils.currentUserIsRoot()){
           return queryAllMenus();
        }
        else{
            return queryAllMenusForUser(WebSecurityUtils.currentUser().getId());
        }
    }

    @Transactional
    @Override
    public User addUser(UserForm.Add user) {
        Assert.notNull(user,"要添加的用户信息不能为空！");
        User havedUser = userDao.findByAccount(user.getAccount());
        if(havedUser != null){
            throw new IllegalArgumentException("创建用户失败：用户账号["+user.getAccount()+"]已存在，请换一个用户名试下！");
        }
        User userEntity = DozerConfiguration.BeanCopyUtils.copy(user,User.class);
        userEntity.setStatus(User.UserStatus.active);
        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        if(CollectionUtils.isNotEmpty(userEntity.getRoles())){
            Role roleEntity = null;
            Set<Role> roles = new HashSet<>();
            for(Role role : userEntity.getRoles()){
                roleEntity = roleDao.findOne(role.getId());
                Assert.notNull(roleEntity,"用户关联的角色[id="+role.getId()+"]不存在或已被删除，请刷新页面重试！");
                roles.add(roleEntity);
            }
            userEntity.setRoles(roles);
        }
        userEntity = userDao.save(userEntity);
        systemEventPublishService.publishSysLogEvent(new UserAddEvent(userEntity));
        return userEntity;
    }

    @Transactional
    @Override
    public User updateUser(Integer id,UserForm.Update user) {
        Assert.notNull(id,"要修改信息的用户id不能为空！");
        User userEntity = userDao.findById(id);
        if(userEntity == null){
            throw new IllegalStateException("修改用户信息失败，该用户[id="+id+"]不存在或已被删除");
        }
        //代表密码被修改了
        if(!StringUtils.equals(user.getPassword(),userEntity.getPassword())){
            userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        //修改名字
        userEntity.setName(user.getName());
        Set<RoleForm.Reference> roles =  user.getRoles();
        if(CollectionUtils.isEmpty(roles)){
            userEntity.setRoles(Collections.EMPTY_SET);
        }
        else {
            if(userEntity.getRoles() == null){
                userEntity.setRoles(new HashSet<>());
            }
            RoleForm.Reference found;

            for(Role role : new HashSet<>(userEntity.getRoles())){
                found = Iterators.find(roles.iterator(), new Predicate<RoleForm.Reference>() {
                    @Override
                    public boolean apply(RoleForm.Reference input) {
                        if(input.getId() == role.getId())
                            return true;
                        else
                            return false;
                    }
                },null);
                if(found != null){
                    roles.remove(found);
                }
                else{
                    userEntity.getRoles().remove(role);
                }
            }
            //处理新增
            Role roleEntity = null;
            for(RoleForm.Reference roleForm : roles){
                roleEntity = roleDao.findOne(roleForm.getId());
                Assert.notNull(roleEntity,"新关联的角色[id="+roleForm.getId()+"]不存在或已被删除，请刷新页面重试！");
                userEntity.getRoles().add(roleEntity);
            }
        }
        userEntity = userDao.save(userEntity);
        systemEventPublishService.publishSysLogEvent(new UserUpdateEvent(userEntity));
        return userEntity;
    }

    @Transactional
    @Override
    public void deleteUser(Integer id) {
        User user = userDao.findById(id);
        if(user == null){
            throw new IllegalStateException("删除用户失败：用户[id="+id+"]不存在或已被删除，请刷新页面重试！");
        }
        //做逻辑删除
        user.setStatus(User.UserStatus.delete);
        userDao.save(user);
        systemEventPublishService.publishSysLogEvent(new UserDeleteEvent(user));
    }

    @Transactional
    @Override
    public void deleteRole(Integer id) {
        Role role = roleDao.findById(id);
        Assert.notNull(role,"该角色[id="+id+"]不存在或已被删除，请刷新页面重试！");
        Assert.isTrue(!WebSecurityUtils.isRoot(role.getName()),"root角色不能被删除！");
        Integer count = userDao.countUserFor(id);
        Assert.isTrue(count == null || count <= 0,"该角色["+role.getName()+"]已被用户使用，请删除与相关用户的关联后再试！");
        roleDao.delete(id);
    }

    @Transactional
    @Override
    public Role updateRole(Integer id, RoleForm.Update roleForm) {
        Assert.notNull(id,"要修改信息的角色id不能为空！");
        Role roleEntity = roleDao.findById(id);
        Assert.notNull(roleEntity,"该角色[id="+id+"]不存在或已被删除，请刷新页面重试！");
        Assert.isTrue(!WebSecurityUtils.isRoot(roleEntity.getName()),"root角色不允许修改！");
        //只让其修改角色描述
        roleEntity.setDesc(roleForm.getDesc());
        Set<MenuForm.Reference> menuForms = roleForm.getMenus();
        //为空，删除所有关联菜单
        if(CollectionUtils.isEmpty(menuForms)){
            roleEntity.setMenus(Collections.emptySet());
        }
        else {
            if(roleEntity.getMenus() == null){
                roleEntity.setMenus(new HashSet<>());
            }
            MenuForm.Reference found;

            for(Menu menu : new HashSet<>(roleEntity.getMenus())){
                found = Iterators.find(menuForms.iterator(), new Predicate<MenuForm.Reference>() {
                    @Override
                    public boolean apply(MenuForm.Reference input) {
                        if(input.getId() == menu.getId())
                            return true;
                        else
                            return false;
                    }
                },null);
                if(found != null){
                    menuForms.remove(found);
                }
                else{
                    roleEntity.getMenus().remove(menu);
                }
            }
            //处理新增
            Menu menuEntity = null;
            for(MenuForm.Reference menuForm : menuForms){
                menuEntity = menuDao.findOne(menuForm.getId());
                Assert.notNull(menuEntity,"新关联的菜单[id="+menuForm.getId()+"]不存在或已被删除，请刷新页面重试！");
                roleEntity.getMenus().add(menuEntity);
            }
        }
        return roleDao.save(roleEntity);
    }

    @Transactional
    @Override
    public Role addRole(RoleForm.Add roleForm) {
        Assert.notNull(roleForm,"要添加的角色信息不能为空！");
        Role haveRole = roleDao.findByName(roleForm.getName());
        Assert.isNull(haveRole,"要添加的角色名称["+roleForm.getName()+"]已存在!");
        Role roleEntity = DozerConfiguration.BeanCopyUtils.copy(roleForm,Role.class);
        if(CollectionUtils.isNotEmpty(roleEntity.getMenus())){
            Set<Menu> menus = new HashSet<>();
            Menu menuEntity = null;
            for(Menu menu : roleEntity.getMenus()){
                menuEntity = menuDao.findOne(menu.getId());
                Assert.notNull(menuEntity,"关联角色的菜单项[id="+menu.getId()+"]不存在或已被删除，请刷新页面重试！");
                menus.add(menuEntity);
            }
            roleEntity.setMenus(menus);
        }
        roleEntity = roleDao.save(roleEntity);
        return roleEntity;
    }

    @Override
    public List<Role> queryAllRoles() {
        return roleDao.findAll();
    }

    @Override
    public List<Menu> queryMenuFor(Integer roleId) {
        return roleDao.queryMenuFor(roleId);
    }

    @Override
    @Transactional
    public void changePassword(ChangePasswordForm changePasswordForm) {
        User user = WebSecurityUtils.currentUser();
        Assert.notNull(user,"登录后才能修改密码！");
        User userFromDb = userDao.findById(user.getId());
        Assert.notNull(userFromDb,"修改密码失败，当前用户账号不存在["+user.getAccount()+"]" );
        Assert.isTrue(passwordEncoder
                        .matches(changePasswordForm.getOldPassword(),userFromDb.getPassword())
                            ,"输入的旧密码错误！");
    }
}
