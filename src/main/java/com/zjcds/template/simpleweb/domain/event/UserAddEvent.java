package com.zjcds.template.simpleweb.domain.event;


import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import com.zjcds.template.simpleweb.domain.enums.SystemLogEventAction;

/**
 * created date：2017-08-15
 * @author niezhegang
 */
public class UserAddEvent extends SysLogEvent{

    public UserAddEvent(Object source) {
        super(source);
    }

    @Override
    public SystemLogEventAction getAction() {
        return SystemLogEventAction.UserAdd;
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
