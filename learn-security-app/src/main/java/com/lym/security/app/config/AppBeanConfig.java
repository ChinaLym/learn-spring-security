package com.lym.security.app.config;

import com.lym.security.app.handler.AppAuthenticationFailureHandler;
import com.lym.security.app.handler.AppAuthenticationSuccessHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * 面向应用时 bean 配置：几个默认处理器
 *
 * @author lym
 * @since 1.0
 */
@Configuration
public class AppBeanConfig {

    /**
     * 认证成功处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationSuccessHandler.class)
    public AuthenticationSuccessHandler appAuthenticationSuccessHandler(ClientDetailsService clientDetailsService, 
                                                                        AuthorizationServerTokenServices authorizationServerTokenServices) {
        return new AppAuthenticationSuccessHandler(clientDetailsService, authorizationServerTokenServices);
    }

    /**
     * 认证失败处理器
     */
    @Bean
    @ConditionalOnMissingBean(AuthenticationFailureHandler.class)
    public AuthenticationFailureHandler appAuthenticationFailureHandler() {
        return new AppAuthenticationFailureHandler();
    }


    /**
     * TokenStore 相关配置
     */
    /*@Configuration
    @ConditionalOnMissingBean(TokenStore.class)
    public static class TokenStoreConfig {

        *//**
         * 使用 redis 作为 token 存储
         *//*
        @Configuration
        @ConditionalOnProperty(prefix = "lym.security.oauth2", name = "tokenStore", havingValue = "redis")
        public static class RedisConfig {

            @Bean
            public TokenStore redisTokenStore(RedisConnectionFactory redisConnectionFactory) {
                return new RedisTokenStore(redisConnectionFactory);
            }

        }

        *//**
         * 使用 jwt 时的配置，默认生效
         *//*
        @Configuration
        @ConditionalOnProperty(prefix = "lym.security.oauth2", name = "tokenStore", havingValue = "jwt", matchIfMissing = true)
        public static class JwtConfig {

            @Bean
            public TokenStore jwtTokenStore() {
                return new JwtTokenStore(jwtAccessTokenConverter());
            }

            @Bean
            public JwtAccessTokenConverter jwtAccessTokenConverter(SecurityProperties securityProperties){
                JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
                converter.setSigningKey(securityProperties.getOauth2().getJwtSigningKey());
                return converter;
            }

            @Bean
            @ConditionalOnMissingBean(TokenEnhancer.class)
            public TokenEnhancer jwtTokenEnhancer(){
                return new TokenJwtEnhancer();
            }

        }

    }*/

}
