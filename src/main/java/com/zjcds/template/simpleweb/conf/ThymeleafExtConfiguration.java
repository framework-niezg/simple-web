package com.zjcds.template.simpleweb.conf;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;

/**
 * Thymeleaf扩展配置
 * created date：2017-01-04
 * @author niezhegang
 */
@Configuration
//@ConditionalOnClass(SpringTemplateEngine.class)
//@ConditionalOnBean(ThymeleafProperties.class)
@Getter
@Setter
@ConfigurationProperties(value="com.zjcds.thymeleaf",ignoreUnknownFields = true)
public class ThymeleafExtConfiguration {
    @Autowired
    private ApplicationContext applicationContext;

    private String prefix = "classpath:/static/html/";

    private String suffix = ".html";

    private boolean cache = true;

    @Bean
    public SpringResourceTemplateResolver customTemplateResolver() {
        SpringResourceTemplateResolver resolver = new SpringResourceTemplateResolver();
        resolver.setApplicationContext(this.applicationContext);
        resolver.setPrefix(prefix);
        resolver.setSuffix(suffix);
        resolver.setCacheable(cache);
        return resolver;
    }

}
