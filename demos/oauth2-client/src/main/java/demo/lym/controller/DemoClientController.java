package demo.lym.controller;

import demo.lym.dto.DemoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

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
        // 发起 rest 调用，向 resource Server 获取资源

        Map<String, String> param = new HashMap<>();
        param.put(OAuth2AuthorizedClient.class.getName().concat(".CLIENT_REGISTRATION_ID"), "demo");
        return restTemplate.getForEntity("http://resourceServer.com:8000/user", DemoUser.class, param);
    }
}
