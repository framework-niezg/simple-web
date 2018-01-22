package com.zjcds.template.simpleweb.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.zjcds.common.base.domain.BaseBean;
import com.zjcds.template.simpleweb.controller.AuthenticationController;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * created date：2017-09-12
 * @author niezhegang
 */
@Getter
@Setter
@ApiModel(value = "menu",description = "菜单对象")
public class MenuForm extends BaseBean{
    @ApiModelProperty(value = "菜单ID",required = true,readOnly = true)
    private Integer id;
    @ApiModelProperty(value = "菜单名称：最大50字符",required = true)
    private String name;
    @ApiModelProperty(value = "菜单图标：最大50字符",required = false)
    private String icon;
    @ApiModelProperty(value = "菜单url：最大200字符",required = false)
    private String url;
    @ApiModelProperty(value = "菜单编码：最大18字符，3位为一级菜单，可以表示6级菜单",required = false)
    private String code;
    @JsonIgnore
    transient private String href;
    @JsonIgnore
    transient private boolean selected = false;
    private List<MenuForm> children = new ArrayList<>();

    public void addChild(MenuForm menuForm){
        Assert.notNull(menuForm,"添加的菜单项不能为空！");
        if(children == null)
            children = new ArrayList<>();
        children.add(menuForm);
    }

    public void access(MenuAccessor menuAccessor){
        menuAccessor.access(this);
    }

    public String getHref(){
        String ret = null;
        if(StringUtils.isNotBlank(url)){
            StringBuilder sb = new StringBuilder()
                                    .append("/")
                                    .append(AuthenticationController.ModuleAccessPath)
                                    .append("/");
            sb.append(url).append("?code=").append(code);
            ret = sb.toString();
        }
        return ret;
    }

    public boolean isChildOrOwner(String code){
        return StringUtils.startsWith(code,this.code);
    }

    public interface MenuAccessor {
        public void access(MenuForm menuForm);
    }

    @Getter
    @Setter
    @ApiModel(value = "menu",description = "菜单对象")
    public static class Owner {
        @ApiModelProperty(value = "菜单ID",required = true,readOnly = true)
        private Integer id;
        @ApiModelProperty(value = "菜单名称：最大50字符",required = true)
        private String name;
        @ApiModelProperty(value = "菜单图标：最大50字符",required = false)
        private String icon;
        @ApiModelProperty(value = "菜单url：最大200字符",required = false)
        private String url;
        @ApiModelProperty(value = "菜单编码：最大18字符，3位为一级菜单，可以表示6级菜单",required = false)
        private String code;
    }

    @ApiModel(value = "menuReference",description = "菜单关联--菜单ID")
    @Getter
    @Setter
    public static class Reference{
        @ApiModelProperty(value = "菜单ID",required = true,readOnly = true)
        @NotNull
        private Integer id;
    }

}
