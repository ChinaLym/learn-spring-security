package com.lym.security.code.sms;

import com.lym.security.code.dto.ValidateCodeDTO;
import com.lym.security.code.generator.ValidateCodeGenerator;
import com.lym.security.code.sms.propertities.SmsCodeProperties;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码生成器
 *
 * @author lym
 */
public class SmsCodeGenerator implements ValidateCodeGenerator, SmsValidateCodeType {

    private SmsCodeProperties smsCodeProperties;

    public SmsCodeGenerator(SmsCodeProperties smsCodeProperties) {
        this.smsCodeProperties = smsCodeProperties;
    }

    /**
     * 生成 6 位数字验证码
     */
    @Override
    public ValidateCodeDTO generate(ServletWebRequest request) {
        String code = RandomStringUtils.randomNumeric(smsCodeProperties.getLength());
        return new ValidateCodeDTO(code, smsCodeProperties.getExpireIn());
    }

    public SmsCodeProperties getSmsCodeProperties() {
        return smsCodeProperties;
    }

    public void setSmsCodeProperties(SmsCodeProperties smsCodeProperties) {
        this.smsCodeProperties = smsCodeProperties;
    }
}
