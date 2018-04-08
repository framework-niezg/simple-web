package com.zjcds.template.simpleweb.service.impl;

import com.zjcds.template.simpleweb.service.AuthorizeService;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class DelegateAuthorizeService {

    @Setter
    private List<AuthorizeService> delegates;

    public DelegateAuthorizeService() {
        this(new ArrayList<>());
    }

    public DelegateAuthorizeService(List<AuthorizeService> delegates) {
        this.delegates = delegates;
    }

    public boolean check(Authentication authentication, HttpServletRequest request) {
        List<AuthorizeService> authorizeServices = getAuthorizeServices(request);
        if (CollectionUtils.isEmpty(authorizeServices)) {
            return false;
        }
        for (AuthorizeService authorizeService : authorizeServices) {
            if (authorizeService.check(authentication, request)) {
                return true;
            }
        }
        return false;
    }

    private List<AuthorizeService> getAuthorizeServices(HttpServletRequest request) {
        List<AuthorizeService> authorizeServices = new ArrayList<>(delegates.size());
        for (AuthorizeService authorizeService : delegates) {
            if (authorizeService.matches(request)) {
                authorizeServices.add(authorizeService);
            }
        }
        return authorizeServices;
    }

}
