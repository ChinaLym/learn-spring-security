package demo.lym.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.security.KeyPair;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 旧授权服务器（spring-security-oauth2）的一个实例，它使用一个单独的、不旋转的密钥并公开一个JWK端点。
 *
 * 更多详情：
 * <a target="_blank" href="https://docs.spring.io/spring-security-oauth2-boot/docs/current-SNAPSHOT/reference/htmlsingle/">
 * 	Spring Security OAuth Autoconfig's documentation
 * </a>
 *
 * @author Josh Cummings
 * @since 5.1
 */
@EnableAuthorizationServer
@Configuration
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

	AuthenticationManager authenticationManager;
	KeyPair keyPair;
	boolean jwtEnabled;

	public AuthorizationServerConfiguration(
			AuthenticationConfiguration authenticationConfiguration,
			KeyPair keyPair,
			@Value("${security.oauth2.authorizationserver.jwt.enabled:true}") boolean jwtEnabled) throws Exception {

		this.authenticationManager = authenticationConfiguration.getAuthenticationManager();
		this.keyPair = keyPair;
		this.jwtEnabled = jwtEnabled;
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()");
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients)
			throws Exception {
		// @formatter:off

		// 创建四个 默认的 clientDetail
		clients.inMemory()
				.withClient("reader")
					.authorizedGrantTypes("password")
					.secret("secret")
					.scopes("message:read")
					.accessTokenValiditySeconds(600_000_000)
				.and()
					.withClient("writer")
					.authorizedGrantTypes("password")
					.secret("secret")
					.scopes("message:write")
					.accessTokenValiditySeconds(600_000_000)
				.and()
					.withClient("noscopes")
					.authorizedGrantTypes("password")
					.secret("secret")
					.scopes("none")
					.accessTokenValiditySeconds(600_000_000)
				.and()
					.withClient("demo-client-id")
					.secret("secret")
                    .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                    .redirectUris("http://localhost:8080/login/oauth2/code/demo","http://127.0.0.1:8080/login/oauth2/code/demo")
                    .scopes("message:read", "message:write", "user:read")
					.accessTokenValiditySeconds(600_000_000)
				.and()
					.withClient("messaging-client")
					.secret("secret")
                    .authorizedGrantTypes("authorization_code", "password", "client_credentials", "implicit", "refresh_token")
                    .redirectUris("http://localhost:8080/authorized","http://127.0.0.1:8080/authorized")
                    .scopes("message.read", "message.write")
					.accessTokenValiditySeconds(600_000_000);
		// @formatter:on
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
		// @formatter:off
		endpoints
				.authenticationManager(this.authenticationManager)
				.tokenStore(tokenStore());

		if (this.jwtEnabled) {
			endpoints
					.accessTokenConverter(accessTokenConverter());
		}
		// @formatter:on
	}

	@Bean
	public TokenStore tokenStore() {
		if (this.jwtEnabled) {
			return new JwtTokenStore(accessTokenConverter());
		} else {
			return new InMemoryTokenStore();
		}
	}

	@Bean
	public JwtAccessTokenConverter accessTokenConverter() {
		JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
		converter.setKeyPair(this.keyPair);

		DefaultAccessTokenConverter accessTokenConverter = new DefaultAccessTokenConverter();
		accessTokenConverter.setUserTokenConverter(new SubjectAttributeUserTokenConverter());
		converter.setAccessTokenConverter(accessTokenConverter);

		return converter;
	}

}


/**
 * 传统授权服务器不支持用户参数的自定义名称，因此我们需要扩展默认值。默认情况下，它使用属性{@code user_name}，不过最好遵循 jwt 规范
 * <a target="_blank" href="https://tools.ietf.org/html/rfc7519">JWT Specification</a>.
 */
class SubjectAttributeUserTokenConverter extends DefaultUserAuthenticationConverter {
	@Override
	public Map<String, ?> convertUserAuthentication(Authentication authentication) {
		Map<String, Object> response = new LinkedHashMap<>();
		response.put("sub", authentication.getName());
		if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
			response.put(AUTHORITIES, AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
		}
		return response;
	}
}