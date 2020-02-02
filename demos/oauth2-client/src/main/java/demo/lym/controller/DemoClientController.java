package demo.lym.controller;

import demo.lym.dto.DemoUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

/**
 * @author lym
 * @since 1.0
 */
@Controller
public class DemoClientController {
    @Autowired
    RestTemplate restTemplate;

    @GetMapping("/")
    public String index(Model model,
                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                        @AuthenticationPrincipal OAuth2User oauth2User) {
        model.addAttribute("userName", oauth2User.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        return "index";
    }

    /**
     * 尝试从资源服务器获取资源
     */
    @ResponseBody
    @GetMapping("/test")
    public DemoUser getResource() {
        // 发起 rest 调用，向 resource Server 获取资源

        return restTemplate.getForObject("http://resourceServer.com:8000/user", DemoUser.class);
    }
}
