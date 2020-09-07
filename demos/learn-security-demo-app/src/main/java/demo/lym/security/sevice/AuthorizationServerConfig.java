package demo.lym.security.sevice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.builders.InMemoryClientDetailsServiceBuilder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationManager;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.ArrayList;
import java.util.List;

/**
 * 授权服务器配置
 *
 * @author lym
 */
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {


    /**
     * 用户提供
     */
    @Autowired
    private UserDetailsService userDetailsService;

    /** fixme lack spring security 提供 */
    //@Autowired
    //private AuthenticationManager authenticationManager;

    /**
     * 根据配置，用户或本框架提供
     */
    @Autowired
    private TokenStore tokenStore;

    /**
     * token 增强器 可选
     */
    @Autowired(required = false)
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    @Autowired(required = false)
    private TokenEnhancer jwtTokenEnhancer;


    /**
     * 认证及token配置
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore)//token存取方式
                .authenticationManager(new OAuth2AuthenticationManager())
                .userDetailsService(userDetailsService);

        if (jwtAccessTokenConverter != null && jwtTokenEnhancer != null) {
            TokenEnhancerChain enhancerChain = new TokenEnhancerChain();
            List<TokenEnhancer> enhancers = new ArrayList<>();
            enhancers.add(jwtTokenEnhancer);
            enhancers.add(jwtAccessTokenConverter);
            enhancerChain.setTokenEnhancers(enhancers);
            endpoints.tokenEnhancer(enhancerChain).accessTokenConverter(jwtAccessTokenConverter);
        }

    }

    /**
     * tokenKey的访问权限表达式配置
     */
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()");
    }

    /**
     * 客户端配置，一般给第三方的，比如允许 lym 应用通过本系统账号登录
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        // 这里将配置放到内存中，实际也可以放到数据库，完成动态配置
        InMemoryClientDetailsServiceBuilder builder = clients.inMemory();

        builder.withClient("lym")
                .secret("demo")
                .redirectUris("http://example.com")

                .authorizedGrantTypes("refresh_token", "authorization_code", "password")
                .accessTokenValiditySeconds(1292000)
                .refreshTokenValiditySeconds(2592000)
                .scopes("all");
    }

}
