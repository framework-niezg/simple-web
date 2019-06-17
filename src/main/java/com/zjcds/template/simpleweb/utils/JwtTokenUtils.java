package com.zjcds.template.simpleweb.utils;

import com.zjcds.template.simpleweb.domain.security.JwtUser;
import com.zjcds.template.simpleweb.domain.security.exception.JwtTokenExpiredException;
import com.zjcds.template.simpleweb.domain.security.exception.JwtTokenMalformedException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.security.Key;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * jwt工具类
 * created date：2019-04-01
 * @author niezhegang
 */
public abstract class JwtTokenUtils {

    public final static String RoleSeparator = "::";

    public final static String RoleClaimKey = "roles";

    public static String generateToken(JwtUser user, String secret) {
        Claims claims = Jwts.claims().setSubject(user.getUserName());
        claims.put(RoleClaimKey,toStringRoleName(user.getRolesNames()) );
        return Jwts.builder()
                .setClaims(claims)
                .signWith(transformKey(secret),SignatureAlgorithm.HS512)
                .setExpiration(user.getExpirationTime())
                .compact();
    }

    public static JwtUser parseToken(String token,String secret) {
        JwtUser user = null;

        try {
            Claims body = Jwts.parser()
                    .setSigningKey(transformKey(secret))
                    .parseClaimsJws(token)
                    .getBody();

            user = new JwtUser();
            user.setUserName(body.getSubject());
            user.setRolesNames(toListRoleName((String) body.get(RoleClaimKey)));
            user.setExpirationTime(body.getExpiration());
        } catch (ExpiredJwtException e) {
            throw new JwtTokenExpiredException();
        } catch (JwtException e) {
            throw new JwtTokenMalformedException();
        }
        return user;
    }

    private static Key transformKey(String secret) {
        return Keys.hmacShaKeyFor(secret.getBytes(Charset.forName("UTF-8")));
    }

    private static  List<String> toListRoleName(String roleNames) {
        if(StringUtils.isNotBlank(roleNames)){
            return Arrays.asList(StringUtils.splitByWholeSeparator(roleNames,RoleSeparator));
        }
        else {
            return Collections.emptyList();
        }
    }

    private static String toStringRoleName(List<String> roleNames) {
       if(CollectionUtils.isEmpty(roleNames))
           return "";
       else
           return StringUtils.join(roleNames,RoleSeparator);
    }

}
