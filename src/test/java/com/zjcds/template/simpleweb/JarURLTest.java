package com.zjcds.template.simpleweb;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.io.InputStream;
import java.net.URI;

/**
 * created date：2018-03-27
 *
 * @author niezhegang
 */
public class JarURLTest extends SpringBootTestSupport{
    @Autowired
    private ResourceLoader resourceLoader;
    @Test
    public void name() throws Exception{
        Assert.assertNotNull(resourceLoader);
//        URI jarUrl = new URI("file:/D:/JAVA/Maven/repository/com/zjcds/common/cds-starter-syslog/2.1-SNAPSHOT/cds-starter-syslog-2.1-SNAPSHOT.jar!/sql/syslog/H2_1.sql");
        Resource resource = resourceLoader.getResource("classpath:/sql/syslog/H2_1.sql");
        InputStream is = resource.getInputStream();
        IOUtils.copy(is,System.out);
//        File file = resource.getFile();
//        System.out.println(file.exists());
    }
}
