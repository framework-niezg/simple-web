package com.zjcds.template.simpleweb.jpa.impl;

import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.jpa.PageResult;
import com.zjcds.common.jpa.annotation.NearestEntityGraph;

import com.zjcds.template.simpleweb.domain.entity.User;
import com.zjcds.template.simpleweb.jpa.UserDao;
import com.zjcds.template.simpleweb.jpa.UserDaoCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * created date：2017-09-04
 *
 * @author niezhegang
 */
public class UserDaoImpl implements UserDaoCustom {

    @Autowired
    private UserDao userDao;

    @Override
    @NearestEntityGraph
    @EntityGraph(attributePaths = "roles")
    public PageResult<User> findAllForFetchRole(Paging paging, List<String> queryString, List<String> orderBy) {
        return userDao.findAll(paging,queryString,orderBy);
    }

}
