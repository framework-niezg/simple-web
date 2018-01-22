package com.zjcds.template.simpleweb.jpa;

import com.zjcds.common.jpa.CustomRepostory;

import com.zjcds.template.simpleweb.domain.entity.Menu;
import com.zjcds.template.simpleweb.domain.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

/**
 * created date：2017-08-28
 * @author niezhegang
 */
public interface UserDao extends CustomRepostory<User,Integer>,UserDaoCustom {
    /**
     * 根据账号名查询用户
     * @param account
     * @return
     */
    @EntityGraph(attributePaths = "roles")
    public User findByAccount(String account);

    @EntityGraph(attributePaths = {"roles"})
    @Query("select u from User u where u.id = :id")
    public User findById(@Param("id") Integer id);

    @Query("select distinct m from User u left join u.roles r left join r.menus m where u.id = :id")
    public Set<Menu> findMenusForUser(@Param("id") Integer id);

    @Query("select count(u) from User u left join u.roles r where u.status='active' and r.id = :roleId")
    public Integer countUserFor(@Param("roleId") Integer roleId);

}
