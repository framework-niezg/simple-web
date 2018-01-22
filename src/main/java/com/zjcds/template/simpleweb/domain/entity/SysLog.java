package com.zjcds.template.simpleweb.domain.entity;

import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;

@Setter
@Getter
@Entity
@Table(name = "t_sys_log")
public class SysLog {
    @Id
    @TableGenerator(name = "idGenerator",table = "t_id_generator",pkColumnName = "id_key",pkColumnValue = "sysLog",valueColumnName = "id_value")
    @GeneratedValue(generator = "idGenerator",strategy = GenerationType.TABLE)
    private Long id;

    @Column(name = "operation_user")
    private String operationUser;
    /**记录时间*/
    @Column(name = "create_time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;
    /**记录行为*/
    private String action;
    /**日志详情*/
    private String detail;

    public static Builder newBuilder(){
        return new Builder();
    }

    /**构建一条日志信息*/
    public static SysLog createSysLog(String action,String detail){
        return newBuilder()
                .action(action)
                .detail(detail)
                .build();
    }

    /**构建一条日志信息*/
    public static SysLog createSysLog(String operationUser,String action,String detail){
        return newBuilder()
                .operationUser(operationUser)
                .action(action)
                .detail(detail)
                .build();
    }


    public static class Builder {

        private String operationUser;

        private Date createTime;

        private String action;

        private String detail;

        public Builder operationUser(String operationUser){
            this.operationUser = operationUser;
            return this;
        }

        public Builder createTime(Date createTime){
            this.createTime = createTime;
            return this;
        }

        public Builder action(String action){
            this.action = action;
            return this;
        }

        public Builder detail(String detail){
            this.detail = detail;
            return this;
        }

        public SysLog build(){
            SysLog sysLog = new SysLog();
            if(StringUtils.isBlank(operationUser))
                operationUser = WebSecurityUtils.currentUserName();
            sysLog.setOperationUser(operationUser);
            Assert.hasText(action,"记录的日志行为不能为空！");
            sysLog.setAction(this.action);
            Assert.hasText(detail,"记录的日志行为详情不能为空！");
            sysLog.setDetail(this.detail);
            if(createTime == null){
                sysLog.setCreateTime(new Date());
            }
            else {
                sysLog.setCreateTime(this.createTime);
            }
            return sysLog;
        }
    }

}