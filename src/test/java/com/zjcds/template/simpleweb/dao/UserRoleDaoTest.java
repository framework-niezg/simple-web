package com.zjcds.template.simpleweb.dao;

import com.zjcds.common.dozer.BeanPropertyCopyUtils;
import com.zjcds.template.simpleweb.DaoTestSupport;
import com.zjcds.template.simpleweb.dao.jpa.um.MenuDao;
import com.zjcds.template.simpleweb.dao.jpa.um.RoleDao;
import com.zjcds.template.simpleweb.dao.jpa.um.UserDao;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.List;

/**
 * created date：2017-08-28
 *
 * @author niezhegang
 */
public class UserRoleDaoTest extends DaoTestSupport {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;
    @Before
    public void init() throws Exception {

    }

    @Test
    public void testName221() throws Exception {
        User user = new User();
        user.setName("张三");
        user.setAccount("zhangsan");
        user.setPassword("******");
//        user.setCreateTime(new Date());
//        user.setModifyTime(new Date());
        User user1 = userDao.save(user);
        userDao.flush();
        Assert.assertNotNull(user.getId());
        Assert.assertNotNull(user.getCreateTime());
        Assert.assertNotNull(user.getModifyTime());
        System.out.println(user.getCreateTime());
        System.out.println(user.getModifyTime());
        Thread.sleep(2000L);
        user1.setName("lisi");
        userDao.flush();
        System.out.println(user.getCreateTime());
        System.out.println(user.getModifyTime());

    }

    @Test
    public void testName() throws Exception {
        List<User> users = new ArrayList<>();
        //添加菜单
        Menu menu1 = new Menu();
        menu1.setName("menu1");
        menu1.setCode("010001");
        menu1.setHide(true);
        menu1.setIcon("/test.icon");
        menu1.setUrl("/menu1");
        Menu menu2 = new Menu();
        menu2.setName("menu2");
        menu2.setCode("010002");
        menu2.setHide(true);
        menu2.setIcon("/test1.icon");
        menu2.setUrl("/menu2");
        menuDao.save(menu1);
        menuDao.save(menu2);
        //添加角色
        Role role1 = new Role();
        role1.setName("testRole1");
        roleDao.save(role1);
        Role role2 = new Role();
        role2.setName("testRole2");
        roleDao.save(role2);
        //角色关联菜单
        role1.addMenu(menu1);
        role1.addMenu(menu2);
        role2.addMenu(menu1);
        role2.addMenu(menu2);
        //添加用户
        User user = new User();
        user.setName("张三");
        user.setAccount("zhangsan");
        user.setPassword("******");
        //用户关联角色
        user.addRole(role1);
        user.addRole(role2);
        users.add(user);
        user = new User();
        user.setName("李四");
        user.setAccount("lisi");
        user.setPassword("******");
        users.add(user);
        users = userDao.save(users);
        userDao.flush();
//        user = userDao.findByAccount("zhangsan");
//        Assert.assertNotNull(user);
//        Assert.assertEquals(users.get(0),user);
//        Assert.assertEquals(2,user.getRoles().size());
//        user.getRoles().forEach(role -> System.out.println(role.getMenus()));
//
//        Role roleForRemove  = new Role();
//        roleForRemove.setId(role1.getId());
//        user.removeRole(roleForRemove);
//        userDao.save(user);
//        user = userDao.findByAccount("zhangsan");
//        Assert.assertEquals(1,user.getRoles().size());
//        userDao.flush();
    }

    @Test
    public void testFindMenusForUser() throws Exception {
        userDao.findMenusForUser(12);

    }

    @Test
    //@Rollback(false)
    public void testGenerateMenu() throws Exception {
        List<Menu> menus = new ArrayList<>();
        //添加菜单
        Menu menu = new Menu();
        menu.setName("交通运行监测");
        menu.setCode("010");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-shujutongji");
        menu.setUrl("monitoring");
        menus.add(menu);

        menu = new Menu();
        menu.setName("统计运行分析");
        menu.setCode("020");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-shujutongji");
        menus.add(menu);

        menu = new Menu();
        menu.setName("统计分析");
        menu.setCode("020010");
        menu.setHide(false);
        menu.setUrl("analysis/operation");
        menus.add(menu);

        menu = new Menu();
        menu.setName("统计测绘图线");
        menu.setCode("020020");
        menu.setHide(false);
        menu.setUrl("analysis/operation");
        menus.add(menu);

        menu = new Menu();
        menu.setName("统计报表");
        menu.setCode("020030");
        menu.setHide(false);
        menu.setUrl("analysis/report");
        menus.add(menu);

        menu = new Menu();
        menu.setName("统计归档报告书");
        menu.setCode("020040");
        menu.setHide(false);
        menu.setUrl("analysis/file");
        menus.add(menu);

        menu = new Menu();
        menu.setName("企业信息管理");
        menu.setCode("030");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-wuliu");
        menus.add(menu);

        menu = new Menu();
        menu.setName("信息管理");
        menu.setCode("030010");
        menu.setHide(false);
        menu.setUrl("enterprise/info");
        menus.add(menu);

        menu = new Menu();
        menu.setName("企业人员管理");
        menu.setCode("030020");
        menu.setHide(false);
        menu.setUrl("enterprise/user");
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆违章信息数据");
        menu.setCode("030030");
        menu.setHide(false);
        menu.setUrl("enterprise/car");
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆丢失");
        menu.setCode("030040");
        menu.setHide(false);
        menu.setUrl("enterprise/lose");
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆信息管理");
        menu.setCode("040");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-wuliu");
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆号牌管理");
        menu.setCode("040010");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆驾驶员信息");
        menu.setCode("040020");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆违章信息数据");
        menu.setCode("040030");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆丢失");
        menu.setCode("040040");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆GPS数据导航");
        menu.setCode("040050");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("车辆导流数据");
        menu.setCode("040060");
        menu.setHide(false);
        menus.add(menu);

        menu = new Menu();
        menu.setName("驾驶信息管理");
        menu.setCode("050");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-user");
        menus.add(menu);

        menu = new Menu();
        menu.setName("平台数据对接");
        menu.setCode("060");
        menu.setHide(false);
        menu.setIcon("Hui-iconfont-link");
        menus.add(menu);
        menuDao.save(menus);
        menuDao.flush();
    }

    @Test
    public void testCopyBean() throws Exception {
        Menu menu = new Menu();
        menu.setName("车辆违章信息数据");
        menu.setCode("030030");
        menu.setHide(false);
        menu.setUrl("enterprise/car");
        MenuForm menuForm = BeanPropertyCopyUtils.copy(menu, MenuForm.class);
        System.out.println(menuForm);

    }

    @Test
    public void testName2() throws Exception {
        User user = userDao.findById(1);
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        System.out.println(user);
    }
}
