package com.zjcds.template.simpleweb.domain.entity;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.zjcds.common.jpa.domain.CreateModifyTime;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * created date：2017-08-28
 * @author niezhegang
 */
@Entity
@Table(name = "T_ROLE")
public class Role extends CreateModifyTime {

    private Integer id;

    private String name;

    private String desc;



    private Set<Menu> menus = new HashSet<>();

    @Id
    @Column(name = "ID")
    @TableGenerator(name = "idGenerator",table = "t_id_generator",pkColumnName = "id_key",pkColumnValue = "role",valueColumnName = "id_value")
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
    @Column(name = "DESCRIPTION")
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @JoinTable(name = "r_role_menu",
            joinColumns = {@JoinColumn(name="role_id")},
            inverseJoinColumns = {@JoinColumn(name = "menu_id")}
    )
    @ManyToMany(fetch = FetchType.LAZY)
    public Set<Menu> getMenus() {
        return menus;
    }

    public void setMenus(Set<Menu> menus) {
        this.menus = menus;
    }

    /**
     * 增加一个关联菜单
     * @param menu
     */
    public void addMenu(Menu menu) {
        Assert.isTrue(menu != null,"添加的关联菜单为空！");
        if(menus == null)
            menus = new HashSet<>();
        menus.add(menu);
    }

    /**
     * 移除一个关联菜单
     * @param menu
     * @return
     */
    public boolean removeMenu(Menu menu) {
        if(menu == null || menu.getId() == null){
            return false;
        }

        boolean ret = false;
        Menu found = null;
        if(getMenus() != null) {
            found = Iterators.find(getMenus().iterator(), new Predicate<Menu>() {
                @Override
                public boolean apply(Menu input) {
                    if (input.getId().equals(menu.getId()))
                        return true;
                    else
                        return false;
                }
            }, null);
            if (found != null) {
                getMenus().remove(found);
                ret = true;
            }
        }
        return ret;
    }

}
