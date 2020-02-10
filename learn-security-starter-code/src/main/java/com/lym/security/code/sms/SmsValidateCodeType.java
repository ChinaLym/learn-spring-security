package com.lym.security.code.sms;

import com.lym.security.code.ValidateCodeType;
import com.lym.security.code.consts.ValidateCodeConsts;

/**
 * 短信验证码类型
 * 统一实现短信验证码相关的 getType() 方法
 *
 * @author lym
 * @since 1.0
 */
public interface SmsValidateCodeType extends ValidateCodeType {

    /**
     * 类型名称，处理器标识，请求参数名
     * @return 不能为空字符串
     */
    @Override
    default String getType(){
        return ValidateCodeConsts.SMS;
    }

}
