package com.zjcds.template.simpleweb.jpa;

import com.zjcds.common.base.domain.page.Paging;
import com.zjcds.common.jpa.PageResult;
import com.zjcds.template.simpleweb.domain.entity.User;


import java.util.List;

/**
 * created date：2017-09-04
 * @author niezhegang
 */
public interface UserDaoCustom {


    public PageResult<User> findAllForFetchRole(Paging paging, List<String> queryString, List<String> orderBy);



}
