package com.zjcds.template.simpleweb.conf;

import com.zjcds.template.simpleweb.service.UserService;
import com.zjcds.template.simpleweb.utils.WebSecurityUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * created date：2017-02-08
 * @author niezhegang
 */
@Configuration
@EnableWebSecurity
public class CustomWebSecurityConfiguration implements ApplicationContextAware{
    /**登录url*/
    public final static String LoginUrl = "/login";
    /**登出url*/
    public final static String LogoutUrl = "/logout";
    /**登录成功后跳转url*/
    public final static String DefaultSuccessUrl = "/index";
    /**登录认证失败跳转url*/
    public final static String LoginFailUrl = LoginUrl +"?error";
    /**登录验证处理url*/
    public final static String LoginProcessingUrl = LoginUrl;
    /**登出成功后跳转url*/
    public final static String LogoutSuccessUrl = LoginUrl;

    private ApplicationContext applicationContext;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * The type Form login web security configuration.
     * niezhegang
     */
    @Configuration
    public static class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter {
        @Autowired
        @Qualifier("daoAuthenticationProvider")
        @Setter
        private AuthenticationProvider daoAuthenticationProvider;

        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .loginProcessingUrl(LoginProcessingUrl)
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .loginPage(LoginUrl)
                    .defaultSuccessUrl(DefaultSuccessUrl)
                    .failureUrl(LoginFailUrl)
                .and()
                    .authorizeRequests()
                    .antMatchers("/resources/**").permitAll()
                    .antMatchers("/api/**","/swagger-resources/**","/v2/**").permitAll()
                    .antMatchers(LoginUrl + "*").permitAll()
                    .antMatchers("/druid/**","/mgmt/**").hasRole(WebSecurityUtils.RootUserRole)
                    //.anyRequest().permitAll()
                    .anyRequest().authenticated()
                .and()
                    .csrf().disable();
            configureSessionManage(http);
            configureLogout(http);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.authenticationProvider(daoAuthenticationProvider);
        }

        private void configureSessionManage(HttpSecurity http) throws Exception {
            SessionRegistry sessionRegistry = new SessionRegistryImpl();
            http.sessionManagement()
                    .maximumSessions(10)
                    .sessionRegistry(sessionRegistry)
                    .and()
                    .sessionFixation()
                    .changeSessionId();
            WebSecurityUtils.initSessionRegistry(sessionRegistry);
        }

        /**
         * logout行为配置
         * @param http
         * @throws Exception
         */
        private void configureLogout(HttpSecurity http) throws Exception{
            http.logout()
                    .logoutRequestMatcher(new AntPathRequestMatcher(LogoutUrl))
                    .logoutSuccessUrl(LogoutSuccessUrl)
                    .invalidateHttpSession(true);
        }

//        @Bean
//        public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
//            return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
//        }
    }

    /**
     * User1 details service user details service.
     *
     * @return the user details service
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceDelegate();
    }

    /**
     * Dao authentication provider authentication provider.
     *
     * @return the authentication provider
     */
    @Bean("daoAuthenticationProvider")
    public AuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    /**
     * 默认采用sha-256算法结合其他摘要算法后生成80字节的字符串
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        PasswordEncoder passwordEncoder = new StandardPasswordEncoder();
        return passwordEncoder;
    }


    /**
     * The type User1 details service delegate.
     *
     * @param <D> the type parameter
     */
    public static class UserDetailsServiceDelegate<D extends UserService> implements UserDetailsService{

        @Setter
        @Getter
        @Autowired
        private D delegate;

        @Override
        public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            if(StringUtils.isBlank(username)) {
                throw new UsernameNotFoundException("用户名不能为空!!");
            }
            UserDetails userDetails = delegate.queryUserByName(username);
            if(userDetails == null)
                throw new UsernameNotFoundException("未找到"+username);
            if(!userDetails.isEnabled())
                throw new DisabledException("用户账号"+username+"当前不可用!");
            return userDetails;
        }
    }

//    @Bean
//    public RequestDataValueProcessor requestDataValueProcessor(){
//        return new CsrfRequestDataValueProcessor();
//    }

}
