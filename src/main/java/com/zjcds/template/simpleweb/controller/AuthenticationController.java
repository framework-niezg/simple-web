package com.zjcds.template.simpleweb.controller;


import com.zjcds.template.simpleweb.annotation.UserInjectToController;
import com.zjcds.template.simpleweb.domain.dto.MenuForm;
import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.MenuUtils;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 认证相关功能控制器
 * 包括login、logout等
 * @author niezhegang
 */
@Controller
@UserInjectToController
public class AuthenticationController implements InitializingBean{
    public final static String ModuleAccessPathKey = "com.zjcds.web.menu.accessPath";

    public static String ModuleAccessPath;

    public static String LoginSuccessSkipToUrl;

    @Value("${"+ModuleAccessPathKey+"}")
    private String moduleAccessPath ;

    @Value("${com.zjcds.web.security.loginSuccessSkipToUrl}")
    private String loginSuccessSkipToUrl;

    /**模块的模板文件根目录*/
    private String moduleTemplateFileRootDir = "pages";

    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if(StringUtils.isBlank(moduleAccessPath))
            throw new IllegalStateException("com.zjcds.web.menu.accessPath属性未设置！");
        ModuleAccessPath = moduleAccessPath;
        if(StringUtils.isBlank(loginSuccessSkipToUrl))
            loginSuccessSkipToUrl = "/"+moduleAccessPath+"/"+"index";
        LoginSuccessSkipToUrl = loginSuccessSkipToUrl;
    }

    /**
     * 请求登录表单页面
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model,HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        if(WebSecurityUtils.isLogined())
            return "redirect:"+loginSuccessSkipToUrl;
        else {
            model.addAttribute("errorMessage",WebSecurityUtils.printErrorMessage(httpServletRequest,httpServletResponse));
            return "login";
        }
    }

    /**
     * 登录成功后默认跳转首页
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = {"/index","/"}, method = RequestMethod.GET)
    public String index() {
        return "redirect:"+loginSuccessSkipToUrl;
    }

    @GetMapping(value = {"/${"+ModuleAccessPathKey+"}/**"})
    public String accessMenu(HttpServletRequest request, Model model, @RequestParam(value = "code",required = false) String code){
        List<MenuForm> menuFormList = MenuUtils.transfromToMenuForm(userService.queryMenusForCurrentUser());
        MenuUtils.markSelected(menuFormList,code,getModuleUrl(request));
        model.addAttribute("menus",menuFormList);
        return fetchMenuView(request);
    }

    /**
     * 辅助方法
     * 从菜单模块请求对象从提取出菜单视图
     * @param request
     * @return
     */
    private String fetchMenuView(HttpServletRequest request){
        String moduleUrl = getModuleUrl(request);
        return moduleTemplateFileRootDir +"/"+moduleUrl;
    }

    private String getModuleUrl(HttpServletRequest request){
        String servletPath = request.getServletPath();
        String url = StringUtils.removeStart(servletPath,"/" + moduleAccessPath);
        url = StringUtils.strip(url,"/");
        return url;
    }

}
