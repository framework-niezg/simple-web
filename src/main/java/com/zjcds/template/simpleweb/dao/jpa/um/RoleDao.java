package com.zjcds.template.simpleweb.dao.jpa.um;


import com.zjcds.common.jpa.CustomRepostory;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * created date：2017-08-28
 * @author niezhegang
 */
public interface RoleDao extends CustomRepostory<Role,Integer> {

    @EntityGraph(attributePaths = "menus")
    @Query("select r from Role r where r.id = :id")
    public Role findById(@Param("id") Integer id);

    @Query("select r from Role r where r.name = :name")
    public Role findByName(@Param("name") String name);

    @Query("select m from Role r left join r.menus m where r.id = :roleId and m.hide = false ")
    public List<Menu> queryMenuFor(@Param("roleId") Integer roleId);

}
