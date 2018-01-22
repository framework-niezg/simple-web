package com.zjcds.template.simpleweb.jpa;

import com.zjcds.common.jpa.CustomRepostory;
import com.zjcds.template.simpleweb.domain.entity.SysLog;
import org.springframework.stereotype.Repository;

/**
 * created date：2017-08-14
 * @author niezhegang
 */
@Repository
public interface SysLogDao extends CustomRepostory<SysLog,Long> {

}
