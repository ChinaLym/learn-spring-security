package com.lym.security.browser.consts;

import com.lym.security.SecurityConst;

/**
 * 浏览器相关常量
 *
 * @author lym
 * @since 1.0
 */
public interface BrowserConsts {

    String CONFIG_PREFIX = SecurityConst.CONFIG_PREFIX + ".browser";

    /**
     * session失效默认的跳转地址
     */
    String PAGE_URL_SESSION_INVALID = "/facade-session-invalid.html";
    /**
     * 登录门户
     */
    String PAGE_URL_SIGN_IN = "/facade-signIn.html";
    /**
     * 注册门户
     */
    String PAGE_URL_SIGN_UP = "/facade-signUp.html";
}
