package com.zjcds.template.simpleweb.common.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * 工程额外的数据信息对象
 * 1、通过扩展endpoint->/projectinfo可获取工程信息
 * 2、默认获取工程名称和工程版本信息，如需获取额外信息，可
 * 通过该类定义一个 Object getInfo()方法来实现
 * created date：2016-12-18
 * @author niezhegang
 */
public class ProjectDataInfo {

    private Map<String,Object> info = new HashMap<>();

    public Object getInfo() {
        info.put("archetype",new ArcheTypeInfo("simple-web-archetype","1.4"));
        return info;
    }

    @Getter
    @Setter
    public static class ArcheTypeInfo{
        private String archeTypeVersion;
        private String archeTypeName;

        public ArcheTypeInfo() {
        }

        public ArcheTypeInfo(String archeTypeName,String archeTypeVersion) {
            this.archeTypeName = archeTypeName;
            this.archeTypeVersion = archeTypeVersion;
        }
    }

}
