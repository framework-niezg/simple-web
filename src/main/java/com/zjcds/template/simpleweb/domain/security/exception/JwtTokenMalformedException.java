package com.zjcds.template.simpleweb.domain.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 非法的token异常
 */
public class JwtTokenMalformedException extends AuthenticationException {

    public JwtTokenMalformedException() {
        super("非法token异常");
    }

}
