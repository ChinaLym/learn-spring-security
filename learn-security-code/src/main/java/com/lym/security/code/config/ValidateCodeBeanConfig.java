package com.lym.security.code.config;

import com.lym.security.code.ValidateCodeFilter;
import com.lym.security.code.ValidateCodeProcessorHolder;
import com.lym.security.code.controller.ValidateCodeController;
import com.lym.security.code.processor.ValidateCodeProcessor;
import com.lym.security.code.store.ValidateCodeStore;
import com.lym.security.code.store.impl.SessionValidateCodeRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import java.util.List;

/**
 * 装配验证码过滤器
 *
 * @author lym
 */
@ConditionalOnBean(ValidateCodeProcessor.class)
@Configuration
public class ValidateCodeBeanConfig {

    /**
     * 验证码相关 spring security 配置类
     */
    @Bean
    public ValidateCodeSecurityConfig validateCodeSecurityConfig(ValidateCodeFilter validateCodeFilter) {
        return new ValidateCodeSecurityConfig(validateCodeFilter);
    }

    /**
     * 验证码处理器 Holder
     * 并装配进默认的 Controller 中
     */
    @Bean
    public ValidateCodeProcessorHolder validateCodeProcessorHolder(List<ValidateCodeProcessor> validateCodeProcessors,
                                                                   @Nullable ValidateCodeController validateCodeController) {
        ValidateCodeProcessorHolder validateCodeProcessorHolder = new ValidateCodeProcessorHolder(validateCodeProcessors);
        if (validateCodeController != null) {
            validateCodeController.setValidateCodeProcessorHolder(validateCodeProcessorHolder);
        }
        return validateCodeProcessorHolder;
    }

    /**
     * 验证码过滤器
     */
    @Bean
    public ValidateCodeFilter validateCodeFilter(ValidateCodeProcessorHolder validateCodeProcessorHolder,
                                                 AuthenticationFailureHandler authenticationFailureHandler) {
        return new ValidateCodeFilter(authenticationFailureHandler, validateCodeProcessorHolder);
    }

    // ----------------- ValidateCodeStore 默认实现 --------------------

    @ConditionalOnMissingBean(ValidateCodeStore.class)
    @Configuration
    public static class ValidateCodeStoreConfig {
        /*@ConditionalOnMissingBean(RedisTemplate.class)
        @ConditionalOnClass(RedisTemplate.class)
        @Bean
        public RedisTemplate redisTemplate(){
            return new RedisTemplate();
        }

        @Bean
        @ConditionalOnClass(RedisTemplate.class)
        public ValidateCodeStore RedisValidateCodeRepository(RedisTemplate redisTemplate){
            return new RedisValidateCodeRepository(redisTemplate);
        }*/

        @Bean
        @ConditionalOnMissingBean(ValidateCodeStore.class)
        public ValidateCodeStore sessionValidateCodeRepository() {
            return new SessionValidateCodeRepository();
        }
    }

}
