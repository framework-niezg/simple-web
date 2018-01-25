package com.zjcds.template.simpleweb.domain.event;


import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.domain.enums.SystemLogEventAction;

/**
 * created date：2017-09-25
 * @author niezhegang
 */
public class UserUpdateEvent extends SysLogEvent{

    public UserUpdateEvent(Object source) {
        super(source);
    }

    @Override
    public SystemLogEventAction getAction() {
        return SystemLogEventAction.UserUpdate;
    }

    @Override
    public String getDetail() {
        if(source != null && source instanceof User){
            return getAction().getSystemLogEventDetail(source);
        }
        else {
            return super.getDetail();
        }
    }
}
