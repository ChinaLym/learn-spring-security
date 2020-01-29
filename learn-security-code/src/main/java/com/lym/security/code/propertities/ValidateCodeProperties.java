package com.lym.security.code.propertities;

import com.lym.security.SecurityConst;

import java.util.Arrays;
import java.util.List;

/**
 * 验证码基础配置项
 *
 * @author lym
 * @since 1.0
 */
public abstract class ValidateCodeProperties {

    /**
     * 验证码长度
     */
    private int length = 6;

    /**
     * 验证码有效时间，默认 10 分钟
     */
    private int expireIn = 60 * 10;

    /**
     * 需要校验验证码的 url 路径，支持通配符
     */
    private List<String> urls;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getExpireIn() {
        return expireIn;
    }

    public void setExpireIn(int expireIn) {
        this.expireIn = expireIn;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

}
