package com.zjcds.template.simpleweb.domain.listener;

import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.domain.event.SysLogEvent;
import com.zjcds.template.simpleweb.domain.event.UserAddEvent;
import com.zjcds.template.simpleweb.domain.event.UserDeleteEvent;
import com.zjcds.template.simpleweb.domain.event.UserUpdateEvent;
import com.zjcds.template.simpleweb.service.SysLogService;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * created date：2017-08-15
 * @author niezhegang
 */
@Service
public class SystemEventListener {
    @Autowired
    private SysLogService sysLogService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @EventListener
    @Async
    public void sysLogEvent(SysLogEvent sysLogEvent){
        sysLogService.saveSysLog(sysLogEvent.getOperationUser()
                                ,sysLogEvent.getAction().getName()
                                ,sysLogEvent.getDetail());
    }

    @EventListener
    public void userAdd(UserAddEvent userAddEvent){
        logger.debug("用户添加事件发生");
    }

    @EventListener
    public void userUpdate(UserUpdateEvent userUpdateEvent){
        if(userUpdateEvent.getSource() != null && userUpdateEvent.getSource() instanceof User){
            WebSecurityUtils.invalidateUserSession((User)userUpdateEvent.getSource());
        }
        logger.debug("用户修改事件发生");
    }

    @EventListener
    public void userDelete(UserDeleteEvent userDeleteEvent){
        if(userDeleteEvent.getSource() != null && userDeleteEvent.getSource() instanceof User){
            WebSecurityUtils.invalidateUserSession((User)userDeleteEvent.getSource());
        }
        logger.debug("用户删除事件发生");
    }


}
