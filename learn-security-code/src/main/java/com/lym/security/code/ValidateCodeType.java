package com.lym.security.code;

/**
 * 验证码类型
 *
 * @author lym
 * @since 1.0
 */
public interface ValidateCodeType {

    /**
     * 类型名称，处理器标识，请求参数名
     *
     * @return 不能为空字符串
     */
    String getType();

}
