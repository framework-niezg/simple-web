package com.zjcds.template.simpleweb.domain.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * token过期异常
 */
public class JwtTokenExpiredException extends AuthenticationException {


    public JwtTokenExpiredException() {
        super("Token过期");
    }
}
