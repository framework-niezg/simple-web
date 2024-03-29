package com.zjcds.template.simpleweb.service.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.dozer.BeanPropertyCopyUtils;
import com.zjcds.common.jpa.PageResult;

import com.zjcds.common.syslog.domain.SysLogApplicationEvent;
import com.zjcds.common.syslog.service.SpringEventPublishService;
import com.zjcds.common.syslog.util.LogRegisterUtils;
import com.zjcds.template.simpleweb.conf.SysLogApplicationEventConfiguration;
import com.zjcds.template.simpleweb.domain.dto.ChangePasswordForm;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.dto.RoleForm;
import com.zjcds.template.simpleweb.domain.dto.UserForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.dao.jpa.um.MenuDao;
import com.zjcds.template.simpleweb.dao.jpa.um.RoleDao;
import com.zjcds.template.simpleweb.dao.jpa.um.UserDao;
import com.zjcds.template.simpleweb.domain.event.UserAddEvent;
import com.zjcds.template.simpleweb.domain.event.UserDeleteEvent;
import com.zjcds.template.simpleweb.domain.event.UserUpdateEvent;
import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.*;

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
    private SpringEventPublishService systemEventPublishService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${com.zjcds.web.security.initPassword:123456}")
    private String initPassword;

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
        User userEntity = BeanPropertyCopyUtils.copy(user,User.class);
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
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(UserAddEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_UserAdd))
                .source(userEntity)
                .evaluateVariable("userDetail",userDetail(userEntity))
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
                );
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
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(UserUpdateEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_UserUpdate))
                .source(userEntity)
                .evaluateVariable("userDetail",userDetail(userEntity))
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
        return userEntity;
    }

    private String userDetail(User user) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("id=").append(user.getId()).append(",")
                .append("账号=").append(user.getAccount()).append(",")
                .append("显示名=").append(user.getName()).append(",")
                .append("状态=").append(user.getStatus());
        Set<Role> roles = user.getRoles();
        if(CollectionUtils.isNotEmpty(roles)){
            sb.append(",");
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

    private String roleDetail(Role role) {
        StringBuilder sb = new StringBuilder();
        sb.append("{")
                .append("id=").append(role.getId()).append(",")
                .append("名称=").append(role.getName()).append(",")
                .append("描述=").append(role.getDesc());
        Set<Menu> menus = role.getMenus();
        if(CollectionUtils.isNotEmpty(menus)){
            sb.append(",");
            int i = 0;
            for(Menu menu : menus){
                if(i == 0){
                    sb.append("分配菜单=[");
                }
                else {
                    sb.append(",");
                }
                sb.append(menu.getName());
                i++;
            }
            sb.append("]");
        }
        sb.append("}");
        return sb.toString();
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
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(UserDeleteEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_UserDelete))
                .source(user)
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
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
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(SysLogApplicationEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_RoleDelete))
                .source(role)
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
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
        roleEntity = roleDao.save(roleEntity);
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(SysLogApplicationEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_RoleUpdate))
                .source(roleEntity)
                .evaluateVariable("roleDetail",roleDetail(roleEntity))
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
        return roleEntity;
    }

    @Transactional
    @Override
    public Role addRole(RoleForm.Add roleForm) {
        Assert.notNull(roleForm,"要添加的角色信息不能为空！");
        Role haveRole = roleDao.findByName(roleForm.getName());
        Assert.isNull(haveRole,"要添加的角色名称["+roleForm.getName()+"]已存在!");
        Role roleEntity = BeanPropertyCopyUtils.copy(roleForm,Role.class);
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
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(SysLogApplicationEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_RoleAdd))
                .source(roleEntity)
                .evaluateVariable("roleDetail",roleDetail(roleEntity))
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
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
        userFromDb.setPassword(passwordEncoder.encode(changePasswordForm.getNewPassword()));
        userDao.save(userFromDb);
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(SysLogApplicationEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_PasswordChange))
                .source(userFromDb)
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );

    }

    @Override
    @Transactional
    public String resetUserPassword(Integer id) {
        User user = userDao.findById(id);
        Assert.notNull(user,"未找到对应[id="+id+"]的用户！");
        user.setPassword(passwordEncoder.encode(initPassword));
        userDao.save(user);
        systemEventPublishService.publishApplicationEvent(SysLogApplicationEvent
                .newBuilder(SysLogApplicationEvent.class)
                .logEvent(LogRegisterUtils.getLogEvent(SysLogApplicationEventConfiguration.LogGroup_UM,SysLogApplicationEventConfiguration.LogEvent_UM_PasswordReset))
                .source(user)
                .occurDate(new Date())
                .operationUser(WebSecurityUtils.currentUserName())
                .build()
        );
        return initPassword;
    }
}
