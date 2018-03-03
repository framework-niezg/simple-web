package com.zjcds.template.simpleweb;


import org.junit.Test;
import org.springframework.util.Assert;

/**
 * created date：2018-02-28
 *
 * @author niezhegang
 */
public class ExceptionTest {

    @Test
    public void name() {
        //Exception exception = new IllegalAccessException("test");
        try {
            Assert.notNull(null,"不能为空！");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
