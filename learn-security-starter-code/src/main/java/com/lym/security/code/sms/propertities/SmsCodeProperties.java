package com.lym.security.code.sms.propertities;

import com.lym.security.code.consts.ValidateCodeConsts;
import com.lym.security.code.propertities.ValidateCodeProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 验证码配置项
 *
 * @author lym
 * @since 1.0
 */
@ConfigurationProperties(prefix = ValidateCodeConsts.CONFIG_PREFIX + ".sms")
public class SmsCodeProperties extends ValidateCodeProperties {


}
