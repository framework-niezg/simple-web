package com.zjcds.template.simpleweb.domain.event;


import com.zjcds.common.syslog.domain.SysLogApplicationEvent;

/**
 * created date：2017-09-25
 * @author niezhegang
 */
public class UserDeleteEvent extends SysLogApplicationEvent {

    public UserDeleteEvent(Object source) {
        super(source);
    }

}
