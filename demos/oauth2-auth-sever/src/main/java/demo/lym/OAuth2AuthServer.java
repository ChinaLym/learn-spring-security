package demo.lym;

import demo.lym.dto.DemoUser;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 授权服务器
 * @author lym
 * @since 1.0
 */
@RestController
@SpringBootApplication
public class OAuth2AuthServer {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthServer.class, args);
    }

    @RequestMapping("hello")
    public String hello(){
        return "hi";
    }

    @GetMapping("/user")
    public DemoUser user() {
        return new DemoUser();
    }

}
