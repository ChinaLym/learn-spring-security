package com.lym.security.code.img.config;

import com.lym.security.code.config.ValidateCodeBeanConfig;
import com.lym.security.code.img.ImageCodeGenerator;
import com.lym.security.code.img.ImageCodeProcessor;
import com.lym.security.code.img.propertities.ImageCodeProperties;
import com.lym.security.code.store.ValidateCodeStore;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

/**
 * 图片验证码自动配置
 *
 * @author lym
 * @since 1.0
 */
@Configuration
@AutoConfigureBefore(ValidateCodeBeanConfig.class)
@EnableConfigurationProperties(ImageCodeProperties.class)
public class ImageCodeBeanConfig {

    @Bean
    @ConditionalOnMissingBean(ImageCodeGenerator.class)
    public ImageCodeGenerator imageCodeGenerator(ImageCodeProperties imageCodeProperties) {
        return new ImageCodeGenerator(imageCodeProperties);
    }


    @Bean
    @ConditionalOnMissingBean(ImageCodeProcessor.class)
    public ImageCodeProcessor imageCodeProcessor(ImageCodeProperties imageCodeProperties,
                                                 ImageCodeGenerator imageCodeGenerator,
                                                 ValidateCodeStore validateCodeStore) {

        return new ImageCodeProcessor(imageCodeProperties, imageCodeGenerator, validateCodeStore);

    }

}
