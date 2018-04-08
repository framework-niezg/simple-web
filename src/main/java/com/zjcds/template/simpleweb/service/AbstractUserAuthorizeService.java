package com.zjcds.template.simpleweb.service;

import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractUserAuthorizeService extends AbstractAuthorizeService {

    public abstract boolean check(User user, HttpServletRequest request);

    public AbstractUserAuthorizeService(String pattern) {
        super(pattern);
    }

    public AbstractUserAuthorizeService(int order, String pattern) {
        super(order, pattern);
    }

    @Override
    public boolean check(Authentication authentication, HttpServletRequest request) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            return check(user, request);
        } else {
            return false;
        }
    }
}
