package demo.lym.config.endpoint;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * 传统授权服务器（spring-security-oauth2）不支持 /introspect。用于校验token是否有效以及对应信息。
 * <p>
 * 本类特地支持该 endPoint，以更好地支持的其他样本的回调。
 */
@FrameworkEndpoint
public class IntrospectEndpoint {
    TokenStore tokenStore;

    IntrospectEndpoint(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

    /**
     * 校验accessToken是否有效以及对应信息
     *
     * @param token accessToken
     * @return accessToken的信息
     */
    @PostMapping("/introspect")
    @ResponseBody
    public Map<String, Object> introspect(@RequestParam("token") String token) {
        // 尝试从 accessToken 缓存中取，如果没有，则返回失效
        OAuth2AccessToken accessToken = this.tokenStore.readAccessToken(token);
        Map<String, Object> attributes = new HashMap<>();
        // 判断是否有效
        boolean invalid = accessToken == null || accessToken.isExpired();
        boolean active = !invalid;

        attributes.put("active", active);
        // 若失效，则提前返回
        if (invalid) {
            return attributes;
        }
        // 获取与 accessToken 对应的认证信息填充返回
        OAuth2Authentication authentication = this.tokenStore.readAuthentication(token);

        attributes.put("exp", accessToken.getExpiration().getTime());
        attributes.put("scope", String.join(" ", accessToken.getScope()));
        attributes.put("sub", authentication.getName());

        return attributes;
    }
}
