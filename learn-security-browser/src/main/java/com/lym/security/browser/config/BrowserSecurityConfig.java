package com.lym.security.browser.config;

import com.lym.security.SecurityConst;
import com.lym.security.authentication.config.SmsCodeAuthenticationSecurityConfig;
import com.lym.security.browser.consts.BrowserConsts;
import com.lym.security.authentication.config.FormAuthenticationSecurityConfig;
import com.lym.security.browser.properties.BrowserProperties;
import com.lym.security.code.config.ValidateCodeSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.InvalidSessionStrategy;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

/**
 * 浏览器环境下安全配置主类
 *
 * @author lym
 */
@Configuration
public class BrowserSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BrowserProperties browserProperties;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private ValidateCodeSecurityConfig validateCodeSecurityConfig;

    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

    @Autowired
    private FormAuthenticationSecurityConfig formAuthenticationSecurityConfig;

    @Autowired
    private PersistentTokenRepository persistentTokenRepository;

    @Autowired
    private SessionInformationExpiredStrategy sessionInformationExpiredStrategy;

    @Autowired
    private InvalidSessionStrategy invalidSessionStrategy;

    @Autowired
    private LogoutSuccessHandler logoutSuccessHandler;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        formAuthenticationSecurityConfig.configure(http);

        //apply 方法：<C extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity>> C apply(C configurer)

        http.apply(validateCodeSecurityConfig).and()
                .apply(smsCodeAuthenticationSecurityConfig).and()

                // 记住我配置，采用 spring security 的默认实现
                // 如果想在'记住我'登录时记录日志，可以注册一个InteractiveAuthenticationSuccessEvent事件的监听器
                .rememberMe()
                    .tokenRepository(persistentTokenRepository)//用token拿到用户名
                    .tokenValiditySeconds(browserProperties.getRememberMeSeconds())//token有效时间
                    .userDetailsService(userDetailsService)//认证类
                    .and()

                // 会话管理器
                .sessionManagement()
                    //session 无效策略（首次请求必定无效）
                    .invalidSessionStrategy(invalidSessionStrategy)
                    // 同一个用户在系统中的最大session数
                    .maximumSessions(browserProperties.getSession().getMaximumSessions())
                        // 登录同一个用户达到最大数量后，阻止后来的session登录 还是将原有 session 顶替
                        .maxSessionsPreventsLogin(browserProperties.getSession().isMaxSessionsPreventsLogin())
                        // session 过期策略
                        .expiredSessionStrategy(sessionInformationExpiredStrategy)
                        .and()
                    .and()

                // 退出登录相关配置
                .logout()
                    .logoutUrl(SecurityConst.URL_AUTHENTICATION_CANCEL)
                    .logoutSuccessHandler(logoutSuccessHandler)
                    .deleteCookies("JSESSIONID")
                    .and()


                // 配置校验规则（哪些请求要过滤）
                .authorizeRequests()
                    .antMatchers(
                        // 未认证的跳转
                        SecurityConst.URL_REQUIRE_AUTHENTICATION,

                        // 登录页面
                        browserProperties.getSignUpUrl(),
                        // 获取验证码请求
                        SecurityConst.URL_VALIDATE_CODE,

                        // 登录请求
                            // 用户名、密码登录请求
                        browserProperties.getSignInPage(),
                            // 手机验证码登录请求
                        SecurityConst.URL_AUTHENTICATION_SMS,

                        // 注册页面
                        BrowserConsts.PAGE_URL_SIGN_UP,
                        // 注册请求
                        SecurityConst.URL_REGISTER,

                        // session失效默认的跳转地址
                            // json 响应
                        browserProperties.getSession().getSessionInvalidUrl() + ".json",
                            // 页面响应
                        browserProperties.getSession().getSessionInvalidUrl() + ".html"
                        )
                    .permitAll()

                    // 其余请求全部开启认证（需要登录）
                    .anyRequest().authenticated()
                    .and()

                // 关闭 csrf
                .csrf().disable();

        //authorizeConfigManager.config(http.authorizeRequests());

    }

}
