package com.zjcds.template.simpleweb.service;


import com.zjcds.template.simpleweb.domain.event.SysLogEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * created date：2017-08-15
 * @author niezhegang
 */
@Service
public class SystemEventPublishService implements ApplicationEventPublisherAware{

    private ApplicationEventPublisher applicationEventPublisher;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishSysLogEvent(SysLogEvent sysLogEvent){
        applicationEventPublisher.publishEvent(sysLogEvent);
    }

}
