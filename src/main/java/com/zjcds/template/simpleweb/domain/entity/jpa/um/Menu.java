package com.zjcds.template.simpleweb.domain.entity.jpa.um;

import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * created date：2017-08-28
 * @author niezhegang
 */
@Entity
@Table(name = "T_MENU")
public class Menu {
    private Integer id;
    private String name;
    private String icon;
    private String url;
    private String code;
    private boolean hide;

    @Id
    @Column(name = "ID")
    @TableGenerator(name = "idGenerator",table = "t_id_generator",pkColumnName = "id_key",pkColumnValue = "menu",valueColumnName = "id_value")
    @GeneratedValue(generator = "idGenerator",strategy = GenerationType.TABLE)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "ICON")
    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Basic
    @Column(name = "URL")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Basic
    @Column(name = "CODE")
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Basic
    @Column(name = "HIDE")
    @Type(type = "yes_no")
    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

}
