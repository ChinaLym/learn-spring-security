package com.lym.security;

/**
 * 框架中用到的一些默认值常量（默认值不代表写死，实际可以通过配置修改这些）
 *
 * @author lym
 * @since 1.0
 */
public interface SecurityConst {

    String CONFIG_PREFIX = "lym.security";

    /**
     * 表单方式认证（用户名密码登录）
     */
    String URL_AUTHENTICATION_FORM = "/authentication/form";
    /**
     * 短信验证码认证（短信验证码登录）
     */
    String URL_AUTHENTICATION_SMS = "/authentication/sms";

    /**
     * 注册新用户 url
     */
    String URL_REGISTER = "/user/register";

    /**
     * 取消认证（退出登录）
     */
    String URL_AUTHENTICATION_CANCEL = "/authentication/cancel";


    /**
     * 获取验证码请求 url
     */
    String URL_VALIDATE_CODE = "/code";

    /**
     * 当请求需要身份认证时，默认跳转的url
     */
    String URL_REQUIRE_AUTHENTICATION = "/authentication/require";

    String AUTHENTICATION_SMS_PARAMETER_NAME = "phoneNumber";
}
