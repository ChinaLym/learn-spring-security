package com.lym.security.code.sms.config;

import com.lym.security.code.config.ValidateCodeBeanConfig;
import com.lym.security.code.sms.DefaultSmsCodeSender;
import com.lym.security.code.sms.SmsCodeGenerator;
import com.lym.security.code.sms.SmsCodeProcessor;
import com.lym.security.code.sms.SmsCodeSender;
import com.lym.security.code.sms.propertities.SmsCodeProperties;
import com.lym.security.code.store.ValidateCodeStore;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 短信验证码自动配置
 *
 * @author lym
 * @since 1.0
 */
@Configuration
@AutoConfigureBefore(ValidateCodeBeanConfig.class)
@EnableConfigurationProperties(SmsCodeProperties.class)
public class SmsCodeConfig {

    @Bean
    @ConditionalOnMissingBean(SmsCodeGenerator.class)
    public SmsCodeGenerator smsCodeGenerator(SmsCodeProperties smsCodeProperties) {
        return new SmsCodeGenerator(smsCodeProperties);
    }


    @Bean
    @ConditionalOnMissingBean(SmsCodeProcessor.class)
    public SmsCodeProcessor smsCodeProcessor(SmsCodeProperties smsCodeProperties,
                                             SmsCodeGenerator smsCodeGenerator,
                                             ValidateCodeStore validateCodeStore,
                                             SmsCodeSender smsCodeSender) {

        return new SmsCodeProcessor(smsCodeProperties, smsCodeGenerator, validateCodeStore, smsCodeSender);

    }

    @Bean
    @ConditionalOnMissingBean(SmsCodeSender.class)
    public SmsCodeSender smsCodeSender() {
        return new DefaultSmsCodeSender();
    }

}
