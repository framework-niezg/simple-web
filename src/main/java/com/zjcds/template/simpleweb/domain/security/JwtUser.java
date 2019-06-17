package com.zjcds.template.simpleweb.domain.security;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

/**
 * created date：2019-04-01
 * @author niezhegang
 */
@Getter
@Setter
public class JwtUser {
    private String userName;
    private List<String> rolesNames;
    private Date expirationTime;


}
