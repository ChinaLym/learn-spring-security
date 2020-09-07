package demo.lym.controller;

import demo.lym.dto.DemoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

/**
 * 调用资源服务器
 *
 * @author lym
 * @since 1.0
 */
@Controller
public class DemoClientController {

    @Autowired
    RestTemplate restTemplate;
    @Value("${demo.resource.uri}")
    private String resourceServerAddress;
    @Autowired
    private WebClient webClient;

    /**
     * 尝试从资源服务器获取资源
     * <code>@RegisteredOAuth2AuthorizedClient("demo-auth2-code") OAuth2AuthorizedClient authorizedClient</code> 获取当前请求里的注册的 客户端的信息
     * <code>@CurrentSecurityContext:</code>                           相当于 SecurityContextHolder.getContext()。注解必须加
     * JWT: Authentication authentication：                            相当于 SecurityContextHolder.getContext().getAuthentication()
     * <code>@AuthenticationPrincipal UserDetails userDetails:</code>  相当于 SecurityContextHolder.getContext().getAuthentication().getPrincipal()。注解必须加，否则默认从参数中获取
     */
    @ResponseBody
    @GetMapping("/test")
    public DemoUser getResource() {
        return restTemplate.getForObject(resourceServerAddress + "/user", DemoUser.class);
    }

    /**
     * 测试使用 webClient 调用资源服务器
     */
    @GetMapping(value = "/testwebclient")
    @ResponseBody
    public DemoUser testWebClient(Authentication authentication,
                                  @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        System.out.println(authentication);

        DemoUser rpcResult = webClient.get()
                .uri(resourceServerAddress + "/user")
                // "demo-auth2-code"
                .attributes(clientRegistrationId(authorizedClient.getClientRegistration().getRegistrationId()))
                .retrieve()
                .bodyToMono(DemoUser.class)
                .block();
        return rpcResult;
    }
}
