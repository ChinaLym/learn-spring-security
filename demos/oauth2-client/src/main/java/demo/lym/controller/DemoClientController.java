package demo.lym.controller;

import demo.lym.dto.DemoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

/**
 * 调用资源服务器
 * @author lym
 * @since 1.0
 */
@Controller
public class DemoClientController {
    @Autowired
    RestTemplate restTemplate;

    /**
     * 尝试从资源服务器获取资源
     */
    @ResponseBody
    @GetMapping("/test")
    public ResponseEntity<DemoUser> getResource() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        if (requestAttributes != null) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            //String token = request.getHeader(TokenContext.KEY_OAUTH2_TOKEN);
        }
        // 发起 rest 调用，向 resource Server 获取资源

        Map<String, String> param = new HashMap<>();
        param.put(OAuth2AuthorizedClient.class.getName().concat(".CLIENT_REGISTRATION_ID"), "demo");
        return restTemplate.getForEntity("http://resourceServer.com:8000/user", DemoUser.class, param);
    }

    @Autowired
    private WebClient webClient;


    /** 测试使用 webClient 调用资源服务器 */
    @GetMapping(value = "/testwebclient")
    @ResponseBody
    public DemoUser testWebClient() {
        return webClient.get()
                .uri("http://resourceServer.com/user")
                .attributes(clientRegistrationId("demo"))
                .retrieve()
                .bodyToMono(DemoUser.class)
                .block();
    }
}
