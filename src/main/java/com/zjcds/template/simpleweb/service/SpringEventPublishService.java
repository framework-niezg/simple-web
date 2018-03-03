package com.zjcds.template.simpleweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

/**
 * created date：2017-08-15
 * @author niezhegang
 */
@Service
public class SpringEventPublishService implements ApplicationEventPublisherAware{

    private ApplicationEventPublisher applicationEventPublisher;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 发布spring应用事件
     * @param applicationEvent
     */
    public void publishApplicationEvent(ApplicationEvent applicationEvent) {
        applicationEventPublisher.publishEvent(applicationEvent);
    }

}
