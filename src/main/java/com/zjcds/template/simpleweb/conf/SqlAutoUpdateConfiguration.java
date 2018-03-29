package com.zjcds.template.simpleweb.conf;

import com.zjcds.common.db.au.domain.ModuleProperties;
import com.zjcds.common.db.au.domain.ModulePropertiesConfig;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.zjcds.common.db.au.DBAutoUpdateProperties.DBAutoUpdatePropertiesPath;

/**
 * created date：2018-03-27
 * @author niezhegang
 */
@Configuration
@EnableConfigurationProperties(value = SqlAutoUpdateConfiguration.ModuleDBAutoUpdateProperties.class)
public class SqlAutoUpdateConfiguration {

    /**
     * 核心模块的数据库升级配置
     * @return
     */
    @Bean
    public ModulePropertiesConfig coreModulePropertiesConfig(ModuleDBAutoUpdateProperties moduleDBAutoUpdateProperties) {
        return ModulePropertiesConfig.newBuilder()
                                        .moduleProperties(moduleDBAutoUpdateProperties.getCore())
                                        .build();
    }

    /**
     * 权限模块的数据库升级配置
     * @return
     */
    @Bean
    public ModulePropertiesConfig permissionModulePropertiesConfig(ModuleDBAutoUpdateProperties moduleDBAutoUpdateProperties) {
        return ModulePropertiesConfig.newBuilder()
                .moduleProperties(moduleDBAutoUpdateProperties.getPermission())
                .build();
    }

    /**
     * 业务模块的数据库升级配置
     * @return
     */
    @Bean
    public ModulePropertiesConfig ywModulePropertiesConfig(ModuleDBAutoUpdateProperties moduleDBAutoUpdateProperties) {
        return ModulePropertiesConfig.newBuilder()
                .moduleProperties(moduleDBAutoUpdateProperties.getYw())
                .build();
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = DBAutoUpdatePropertiesPath)
    public static class ModuleDBAutoUpdateProperties {
        @NestedConfigurationProperty
        private ModuleProperties core;
        @NestedConfigurationProperty
        private ModuleProperties permission;
        @NestedConfigurationProperty
        private ModuleProperties yw;
    }
}
