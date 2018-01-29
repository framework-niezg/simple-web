package com.zjcds.template.simpleweb.controller;

import com.zjcds.common.dozer.BeanPropertyCopyUtils;
import com.zjcds.common.jsonview.annotations.JsonViewException;
import com.zjcds.common.jsonview.utils.ResponseResult;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.MenuUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * created date：2017-09-22
 * @author niezhegang
 */
@JsonViewException
@RestController
@RequestMapping("/menus")
@Api(description = "菜单管理类操作")
public class MenuManagerController {
    @Autowired
    private UserService userService;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @GetMapping("/list")
    @ApiOperation(value ="查询所有菜单项，以列表方式进行组织",produces = "application/json;charset=utf-8")
    public ResponseResult<List<MenuForm.Owner>> queryAllMenuForList(){
        Set<Menu> menus = userService.queryAllMenus();
        return new ResponseResult<>(BeanPropertyCopyUtils.copy(MenuUtils.orderMenuCollectionByCode(menus),MenuForm.Owner.class));
    }

    @GetMapping("/cascade")
    @ApiOperation(value ="查询所有菜单项，以层级方式进行组织",produces = "application/json;charset=utf-8")
    public ResponseResult<List<MenuForm>> queryAllMenuForCascade(){
        Set<Menu> menus = userService.queryAllMenus();
        return new ResponseResult<>(MenuUtils.transfromToMenuForm(menus));
    }

}
