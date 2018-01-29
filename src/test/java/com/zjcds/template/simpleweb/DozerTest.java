package com.zjcds.template.simpleweb;

import com.zjcds.common.dozer.BeanPropertyCopyUtils;

import com.zjcds.template.simpleweb.domain.dto.RoleForm;
import com.zjcds.template.simpleweb.domain.dto.UserForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Role;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.User;
import org.apache.commons.collections.CollectionUtils;
import org.dozer.DozerBeanMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.Set;

/**
 * created date：2017-08-30
 *
 * @author niezhegang
 */
public class DozerTest extends DaoTestSupport{

    @Autowired
    private DozerBeanMapper dozerBeanMapper;

;
    @Test
    public void testName() throws Exception {
        UserForm.Add ref = new UserForm.Add();
        ref.setName("haha");
        ref.setPassword("1234");
        ref.setAccount("haha122");
        ref.setRoles(new HashSet<>());
        RoleForm.Reference roleRef = new RoleForm.Reference();
        roleRef.setId(122);
        ref.getRoles().add(roleRef);
        User user = BeanPropertyCopyUtils.copy(ref, User.class);
        Set<Role> roles = user.getRoles();
        for (Role role : roles){
            System.out.println(role.getClass());
        }
    }

    @Test
    public void testName1() throws Exception {
        UserForm.Add ref = new UserForm.Add();
        ref.setName("haha");
        ref.setPassword("1234");
        ref.setAccount("haha122");
        ref.setRoles(new HashSet<>());
        RoleForm.Reference roleRef = new RoleForm.Reference();
        roleRef.setId(122);
        ref.getRoles().add(roleRef);
        User userEntity = BeanPropertyCopyUtils.copy(ref,User.class);
        userEntity.setStatus(User.UserStatus.active);
        userEntity.setPassword("fafa");
        if(CollectionUtils.isNotEmpty(userEntity.getRoles())){
            Role roleEntity = null;
            Set<Role> roles = new HashSet<>();
            for(Role role : userEntity.getRoles()){
                Assert.notNull(role,"用户关联的角色[id="+role.getId()+"]不存在或已被删除，请刷新页面重试！");
                roles.add(role);
            }
            userEntity.setRoles(roles);
        }

    }
}
