package com.zjcds.template.simpleweb.domain.event;


import com.zjcds.common.syslog.domain.SysLogApplicationEvent;

/**
 * created date：2017-09-25
 * @author niezhegang
 */
public class UserUpdateEvent extends SysLogApplicationEvent{

    public UserUpdateEvent(Object source) {
        super(source);
    }

}
