package com.zjcds.template.simpleweb.domain.entity.jpa.um;

import com.google.common.base.Predicate;
import com.google.common.collect.Iterators;
import com.zjcds.common.jpa.domain.CreateModifyTime;
import com.zjcds.template.simpleweb.domain.priv.RoleGrantedAuthority;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * created date：2017-08-28
 * @author niezhegang
 */
@Entity
@Table(name = "T_USER")
public class User extends CreateModifyTime implements UserDetails {
    private Integer id;
    private String account;
    private String name;
    private String password;
    private UserStatus status;
    private Set<Role> roles = new HashSet<>();

    @Id
    @Column(name = "ID")
    @TableGenerator(name = "idGenerator",table = "t_id_generator",pkColumnName = "id_key",pkColumnValue = "user",valueColumnName = "id_value")
    @GeneratedValue(generator = "idGenerator",strategy = GenerationType.TABLE)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "ACCOUNT")
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
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
    @Column(name = "PASSWORD")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "STATUS")
    @Enumerated(EnumType.STRING)
    public UserStatus getStatus() {
        //默认激活状态
        if(status == null){
            status = UserStatus.active;
        }
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new HashSet<>();
        if(roles != null) {
            for(Role role : roles){
                authorities.add(new RoleGrantedAuthority(role.getName()));
            }
        }
        return authorities;
    }

    @Override
    @Transient
    public String getUsername() {
        return account;
    }

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        if(status == null || status != UserStatus.active)
            return false;
        else
            return true;
    }

    @ManyToMany
    @JoinTable(name = "r_user_role",
            joinColumns = {@JoinColumn(name = "user_id" )},
            inverseJoinColumns = {@JoinColumn(name = "role_id")})
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public void addRole(Role role) {
        Assert.isTrue(role != null,"添加的关联角色为空！");
        if(roles == null)
            roles = new HashSet<>();
        roles.add(role);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        return account.equals(user.account);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + account.hashCode();
        return result;
    }

    /**
     * 移除一个角色
     * @param role
     * @return
     */
    public boolean removeRole(Role role) {
        if(role == null || role.getId() == null){
            //throw new IllegalArgumentException("要移除的角色为空！");
            return false;
        }
        boolean ret = false;
        Role found = null;
        if(getRoles() != null) {
            found = Iterators.find(getRoles().iterator(), new Predicate<Role>() {
                @Override
                public boolean apply(Role input) {
                    if (input.getId().equals(role.getId()))
                        return true;
                    else
                        return false;
                }
            }, null);
            if (found != null) {
                getRoles().remove(found);
                ret = true;
            }
        }
        return ret;
    }

    public static enum UserStatus {
        /**激活状态*/
        active,
        /**删除状态，做逻辑删除*/
        delete
    }

}
