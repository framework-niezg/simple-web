package com.zjcds.template.simpleweb.utils;

import com.google.common.base.Predicate;
import com.google.common.collect.FluentIterable;
import com.zjcds.common.dozer.BeanPropertyCopyUtils;

import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.domain.entity.jpa.um.Menu;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.util.*;

/**
 * created date：2017-09-18
 * @author niezhegang
 */
public abstract class MenuUtils {

    private static Logger logger = LoggerFactory.getLogger(MenuUtils.class);

    private static Comparator<Menu> menuComparator = new MenuComparator();

    /**
     * 标记菜单选中状态
     * 有code的使用code判断标记
     * 没有code的使用url
     * @param code
     * @param url
     */
    public static void markSelected(List<MenuForm> menuForms,String code,String url){
        code = processCode(menuForms,code,url);
        if(StringUtils.isBlank(code))
            return;
        markSelectedInner(menuForms,code);
    }

    private static void markSelectedInner(List<MenuForm> menuForms, String code){
        if(CollectionUtils.isNotEmpty(menuForms)){
            for(MenuForm menuForm : menuForms){
                if(menuForm.isChildOrOwner(code)){
                    menuForm.setSelected(true);
                }
                markSelectedInner(menuForm.getChildren(),code);
            }
        }
    }

    private static String processCode(List<MenuForm> menuForms,String code,String url){
        if(StringUtils.isNotBlank(code))
            return code;
        else {
            return processCodeFromUrl(menuForms,url);
        }
    }

    private static String processCodeFromUrl(List<MenuForm> menuForms,String url){
        String ret = null;
        if(CollectionUtils.isNotEmpty(menuForms)) {
            for (MenuForm menuForm : menuForms) {
                if (StringUtils.equals(menuForm.getUrl(), url)) {
                    ret = menuForm.getCode();
                } else {
                    ret = processCodeFromUrl(menuForm.getChildren(), url);
                }
                if (StringUtils.isNotBlank(ret)) {
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * @param menuSet
     * @return
     */
    public static List<MenuForm> transfromToMenuForm(Set<Menu> menuSet) {
        List<MenuForm> menuForms = new ArrayList<>();
        if(CollectionUtils.isNotEmpty(menuSet)) {
            List<Menu> menus = orderMenuCollectionByCode(menuSet);
            List<Menu> rootMenus = findRootMenus(menus);
            MenuForm rootMenuForm = null;
            for(Menu rootMenu : rootMenus){
                //被隐藏的不显示
                if(rootMenu.isHide()){
                    logger.trace("菜单"+rootMenu.getCode()+"被隐藏，将不被显示！");
                }
                else {
                    rootMenuForm = BeanPropertyCopyUtils.copy(rootMenu,MenuForm.class);
                    menuForms.add(rootMenuForm);
                    fillChildren(rootMenuForm , menus);
                }
            }
        }
        return menuForms;
    }

    /**
     * 填充子菜单
     * @param parent
     * @param menus
     */
    private static void fillChildren(final MenuForm parent,List<Menu> menus){
        if(CollectionUtils.isNotEmpty(menus)){
            List<Menu> directChildMenus = FluentIterable.from(menus).filter(new Predicate<Menu>() {
                @Override
                public boolean apply(Menu input) {
                    return isDirectChild(parent.getCode(),input);
                }
            }).toList();
            if(CollectionUtils.isNotEmpty(directChildMenus)){
                MenuForm menuForm = null;
                for (Menu directChildMenu : directChildMenus){
                    if(directChildMenu.isHide()){
                        logger.trace("菜单"+directChildMenu.getCode()+"被隐藏，将不被显示！");
                        continue;
                    }
                    menuForm = BeanPropertyCopyUtils.copy(directChildMenu,MenuForm.class);
                    parent.addChild(menuForm);
                    fillChildren(menuForm,menus);
                }
            }
        }
    }

    private static boolean isDirectChild(String parentCode,Menu menu){
        boolean bret = false;
        if(parentCode.length() == menu.getCode().length() - 3){
            if(StringUtils.equals(parentCode,StringUtils.substring(menu.getCode(),0,parentCode.length()))){
                bret = true;
            }
        }
        return bret;
    }

    /**
     * @param menus 菜单集合：非空并且已按code长度和code顺序进行了排序
     * @return
     */
    private static List<Menu> findRootMenus(List<Menu> menus){
        List<Menu> rootMenus = FluentIterable.from(menus).filter(new Predicate<Menu>() {
            @Override
            public boolean apply(Menu input) {
                return input.getCode().length() == 3;
            }
        }).toList();
        return rootMenus;
    }

    private static void validMenuCode(Collection<Menu> menuSet){
        menuSet.forEach(menu ->{
            if(StringUtils.isBlank(menu.getCode()) || (menu.getCode().length() % 3 != 0)) {
                throw new IllegalArgumentException("菜单[" + menu.getId() + "]编码[" + menu.getCode() + "]不符合规范!");
            }
        });
    }

    /**
     * 排序菜单:按code顺序正序排列菜单
     * @param menus
     * @return
     */
    public static List<Menu> orderMenuCollectionByCode(Collection<Menu> menus){
        Assert.notNull(menus,"要排序的菜单集合不能为空！");
        validMenuCode(menus);
        List<Menu> menuList = new ArrayList<>(menus);
        Collections.sort(menuList,menuComparator);
        return menuList;
    }

    public static class MenuComparator implements Comparator<Menu> {
        @Override
        public int compare(Menu o1, Menu o2) {
            String code1 = o1.getCode();
            String code2 = o2.getCode();
            if(StringUtils.isBlank(code1) || StringUtils.isBlank(code2)){
                return 0;
            }
            return code1.length() - code2.length() == 0 ? code1.compareTo(code2) :  code1.length() - code2.length();
        }
    }
}
