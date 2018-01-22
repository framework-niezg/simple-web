package com.zjcds.template.simpleweb.service;

import com.zjcds.template.simpleweb.domain.entity.SysLog;
import com.zjcds.template.simpleweb.jpa.SysLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统日志服务
 * 重要系统事件入库保存
 * created date：2017-08-07
 * @author niezhegang
 */
@Service
public class SysLogService {

    @Autowired
    private SysLogDao sysLogDao;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Transactional
    public void saveSysLog(String action, String detail){
        logger.debug("发生行为{}，事件详情:{}",action,detail);
        sysLogDao.save(SysLog.createSysLog(action,detail));
    }

    @Transactional
    public void saveSysLog(String operationUser,String action,String detail){
        logger.debug("用户{}发生行为{}，事件详情:{}",operationUser,action,detail);
        sysLogDao.save(SysLog.createSysLog(operationUser,action,detail));
    }

}
