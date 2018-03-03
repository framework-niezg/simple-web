package com.zjcds.template.simpleweb.conf;

import com.zjcds.common.syslog.domain.LogEvent;
import com.zjcds.common.syslog.domain.LogGroup;
import com.zjcds.common.syslog.util.LogRegisterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

/**
 * 注册系统日志
 * created date：2018-03-02
 * @author niezhegang
 */
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE - 5)
public class SysLogApplicationEventConfiguration implements ApplicationRunner {

    private Logger logger = LoggerFactory.getLogger(SysLogApplicationEventConfiguration.class);
    //用户管理日志组
    public static final String LogGroup_UM = "um";
    //新建用户日志事件
    public static final String LogEvent_UM_UserAdd = "userAdd";
    //修改用户信息日志事件
    public static final String LogEvent_UM_UserUpdate = "userUpdate";
    //删除用户日志事件
    public static final String LogEvent_UM_UserDelete = "userDelete";
    //新建角色日志事件
    public static final String LogEvent_UM_RoleAdd = "roleAdd ";
    //修改角色信息日志事件
    public static final String LogEvent_UM_RoleUpdate = "roleUpdate";
    //删除角色日志事件
    public static final String LogEvent_UM_RoleDelete = "roleDelete";
    //重置密码
    public static final String LogEvent_UM_PasswordReset = "passwordReset";
    //密码修改
    public static final String LogEvent_UM_PasswordChange = "passwordChange";
    @Override
    public void run(ApplicationArguments args) throws Exception {
        //注册用户管理事件组
        LogGroup umlogGroup = LogGroup.newBuilder()
                .name("um")
                .displayName("用户管理")
                .build();
        LogRegisterUtils.registerLogGroup(umlogGroup);
        //注册用户管理事件组对应的事件
        registerUMLogEvent(umlogGroup);
    }

    /**
     * 注册用户管理事件组对应的事件
     * @param umlogGroup
     */
    private void registerUMLogEvent(LogGroup umlogGroup) {
        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_UserAdd)
                .displayName("新建用户账号")
                .templateText("管理用户[{{operationUser}}]新建了用户账号[{{eventSource.account}}]，新建账号详情：{{userDetail}}。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_UserUpdate)
                .displayName("修改用户信息")
                .templateText("管理用户[{{operationUser}}]修改用户账号[{{eventSource.account}}]信息，修改后账号详情：{{userDetail}}。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_UserDelete)
                .displayName("删除用户账号")
                .templateText("管理用户[{{operationUser}}]删除了用户账号[{{eventSource.account}}]。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_RoleAdd)
                .displayName("新建角色")
                .templateText("管理用户[{{operationUser}}]新建了用户角色[{{eventSource.name}}]，新建角色详情：{{roleDetail}}。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_RoleUpdate)
                .displayName("修改角色")
                .templateText("管理用户[{{operationUser}}]修改了用户角色[{{eventSource.name}}]，修改后角色详情：{{roleDetail}}。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_RoleDelete)
                .displayName("删除角色")
                .templateText("管理用户[{{operationUser}}]删除了用户角色[{{eventSource.name}}]。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_PasswordReset)
                .displayName("密码重置")
                .templateText("管理用户[{{operationUser}}]将用户[{{eventSource.account}}]密码进行了重置。")
                .build());

        LogRegisterUtils.registerLogEvent(LogEvent.newBuilder()
                .logGroup(umlogGroup)
                .name(LogEvent_UM_PasswordChange)
                .displayName("密码修改")
                .templateText("用户[{{eventSource.account}}]修改了自己的密码。")
                .build());
        logger.debug("注册用户管理事件完成");
    }
}
