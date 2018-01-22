package com.zjcds.template.simpleweb.domain.event;

import com.zjcds.template.simpleweb.domain.enums.SystemLogEventAction;

/**
 * created date：2017-08-15
 *
 * @author niezhegang
 */
public interface LogSource {

    public String getOperationUser();

    public SystemLogEventAction getAction();

    /**日志详情*/
    public String getDetail();
}
