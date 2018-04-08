package com.zjcds.template.simpleweb.service.impl;

import com.zjcds.template.simpleweb.dao.jpa.um.UserDao;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.service.AbstractUserAuthorizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Set;

public class MenuAuthorizeService extends AbstractUserAuthorizeService {

    @Autowired
    private UserDao userDao;

    public MenuAuthorizeService(String pattern) {
        super(pattern);
    }

    public boolean check(User user, HttpServletRequest request) {
        String url = request.getServletPath();
        Set<Menu> menuList = userDao.findMenusForUser(user.getId());
        for (Menu menu : menuList) {
            if (url.equals(menu.getUrl())) {
                return true;
            }
        }
        return false;
    }
}
