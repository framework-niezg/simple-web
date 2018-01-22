package com.zjcds.template.simpleweb.domain.event;

import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import org.springframework.context.ApplicationEvent;

/**
 * created date：2017-08-15
 * @author niezhegang
 */
public abstract class SysLogEvent extends ApplicationEvent implements SystemEvent,LogSource {

    public SysLogEvent(Object source) {
        super(source);
    }

    @Override
    public String getOperationUser() {
        return WebSecurityUtils.currentUserName();
    }

    @Override
    public String getDetail() {
        return getSource() != null ? getSource().toString() : null;
    }
}
