package com.zjcds.template.simpleweb.service;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;

public abstract class AbstractAuthorizeService implements AuthorizeService {

    protected int order;

    private RequestMatcher requestMatcher;

    public AbstractAuthorizeService(String pattern) {
        this(0, pattern);
    }

    public AbstractAuthorizeService(int order, String pattern) {
        this.order = order;
        this.requestMatcher = new AntPathRequestMatcher(pattern);
    }

    @Override
    public boolean matches(HttpServletRequest request) {
        return requestMatcher.matches(request);
    }

    @Override
    public int getOrder() {
        return order;
    }
}
