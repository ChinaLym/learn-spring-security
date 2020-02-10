package com.lym.security.code.sms;

import com.lym.security.SecurityConst;
import com.lym.security.code.consts.ValidateCodeConsts;
import com.lym.security.code.dto.ValidateCodeDTO;
import com.lym.security.code.exception.ValidateCodeException;
import com.lym.security.code.generator.ValidateCodeGenerator;
import com.lym.security.code.processor.AbstractValidateCodeProcessor;
import com.lym.security.code.propertities.ValidateCodeProperties;
import com.lym.security.code.store.ValidateCodeStore;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 短信验证码处理器
 *
 * @author lym
 */
@Component("smsValidateCodeProcessor")
public class SmsCodeProcessor extends AbstractValidateCodeProcessor<ValidateCodeDTO> implements SmsValidateCodeType {

    /**
     * 短信验证码发送器
     */
    private SmsCodeSender smsCodeSender;

    /** 短信验证码只有 POST 请求才能获取（避免直接在浏览器地址栏上直接访问调用） */
    @Override
    protected boolean isPostOnly(){
        return true;
    }

    public SmsCodeProcessor(ValidateCodeProperties validateCodeProperties, ValidateCodeGenerator validateCodeGenerator,
                            ValidateCodeStore validateCodeStore, SmsCodeSender smsCodeSender) {
        super(validateCodeProperties, validateCodeGenerator, validateCodeStore);
        this.smsCodeSender = smsCodeSender;
    }

    @Override
    public void send(ServletWebRequest request, ValidateCodeDTO validateCode) throws ValidateCodeException {
        try {
            String mobile = ServletRequestUtils.getRequiredStringParameter(request.getRequest(), SecurityConst.AUTHENTICATION_SMS_PARAMETER_NAME);
            smsCodeSender.send(mobile, validateCode.getCode());
        } catch (Exception e) {
            throw new ValidateCodeException(e.getMessage(), e);
        }
    }

}
