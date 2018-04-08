package com.zjcds.template.simpleweb.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthorizeService {

    public boolean check(Authentication authentication, HttpServletRequest request);

    public boolean matches(HttpServletRequest request);

    public int getOrder();

}
