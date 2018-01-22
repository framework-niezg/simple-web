package com.zjcds.template.simpleweb.jpa;


import com.zjcds.template.simpleweb.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Set;

/**
 * created date：2017-08-29
 * @author niezhegang
 */
public interface MenuDao extends JpaRepository<Menu,Integer>{

    @Query("select m from Menu m ")
    public Set<Menu> queryAllMenu();


}
