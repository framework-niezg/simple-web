package com.zjcds.template.simpleweb.conf;

import com.zjcds.template.simpleweb.controller.AuthenticationController;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * created date：2017-02-10
 *
 * @author niezhegang
 */
@Configuration
public class CustomErrorMvcConfiguration {

    @Bean
    public ErrorViewResolver customErrorViewResolver(){
        return new CustomErrorViewResolver();
    }

    /**
     * 定制一个错误视图解析器
     */
    public static class CustomErrorViewResolver implements ErrorViewResolver {
        private String errorBasePath = "error/";

        @Override
        public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
            Map modelNew = new HashMap<>(model);
            String errorView = resolveErrorViewName(request,status,modelNew);
            return new ModelAndView(errorView,modelNew);
        }

        /**
         * resolve error view name
         * @param request
         * @param status
         * @param model
         * @return
         */
        private String resolveErrorViewName(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
            String viewName = errorBasePath + "500";
            String errorMessage = "内部服务器错误，请联系管理员！";
            if(status != null){
                if(status == HttpStatus.FORBIDDEN
                           ){
                    viewName = errorBasePath + status.value();
                    errorMessage = "你没有访问该页面的权限！";
                }
                else if(status == HttpStatus.NOT_FOUND){
                    viewName = errorBasePath + status.value();
                    errorMessage = "请求的页面未找到！";
                }
            }
            model.put("errorMessage",errorMessage);
            model.put("backUrl", AuthenticationController.LoginSuccessSkipToUrl);
            return viewName;
        }
    }
}
