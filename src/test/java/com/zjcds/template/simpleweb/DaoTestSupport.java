package com.zjcds.template.simpleweb;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * created date：2017-04-21
 * @author niezhegang
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {BootStrapApplication.class})
@Rollback
public class DaoTestSupport extends AbstractTransactionalJUnit4SpringContextTests {

}
