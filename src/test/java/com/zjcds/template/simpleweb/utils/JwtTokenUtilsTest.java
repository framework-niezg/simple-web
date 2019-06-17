package com.zjcds.template.simpleweb.utils;

import com.zjcds.common.base.utils.DateUtils;
import com.zjcds.template.simpleweb.domain.security.JwtUser;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.Before;
import org.springframework.util.Base64Utils;

import java.time.LocalDate;
import java.util.Arrays;

/** 
* JwtTokenUtils Tester. 
* 
* @author <Authors name> 
* @since <pre>���� 1, 2019</pre> 
* @version 1.0 
*/ 
public class JwtTokenUtilsTest { 
    String secret = "096cwnnb62eils2tn502au4ziu2yj84km1q5vo1e3xb74sqmf67gz1vf9ungwbwz";

    JwtUser jwtUser;

    String keys = "0123456789qwertyuioplkjgfdsazxcvbnm";

    String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiLlvKDkuIkiLCJyb2xlcyI6ImFkbWluOjpndWVzdCIsImV4cCI6MTcxMTkwMDgwMH0.3Q1UPIo7NzsehtcDZYDS-1dRLAhPfKa0Fx2VuYlvEIr-1DNZX6MobQmGfG7l3lk3fUy9J9-2kne-NFySj6TgPQ";

    @Before
    public void setUp() throws Exception {
        jwtUser = new JwtUser();
        jwtUser.setUserName("张三");
        jwtUser.setRolesNames(Arrays.asList(new String[]{"admin","guest"}));
        jwtUser.setExpirationTime(DateUtils.asDate(LocalDate.now().plusYears(5)));
    }

    @Test
    public void randomKey() {
        int length = 64;
        int size = keys.length();
        StringBuilder sb = new StringBuilder();
        for(int i = 0 ; i < 64;i++) {
            sb.append(keys.charAt(RandomUtils.nextInt(0,size)));
        }
        System.out.println(sb.toString());
    }

    @Test
    public void testGenerateToken() {
        Assert.assertEquals(token,
                JwtTokenUtils.generateToken(jwtUser,secret));
    }

    @Test
    public void testParseToken() {
        JwtUser jwtUser = JwtTokenUtils.parseToken(token,secret);
        Assert.assertNotNull(jwtUser);
    }

    @Test
    public void parseBody() throws Exception{
        String url = new String(Base64Utils.decodeFromUrlSafeString("eyJzdWIiOiLlvKDkuIkiLCJyb2xlcyI6ImFkbWluOjpndWVzdCIsImV4cCI6MTcxMTkwMDgwMH0"),"UTF-8");
        System.out.println(url);
    }
}
