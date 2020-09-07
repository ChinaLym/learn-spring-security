package demo.lym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * demo
 * <p>
 * http://demo.com/oauth/authorize?response_type=code&client_id=lym&redirect_uri=http://example.com&scope=all
 *
 * @author lym
 * @since 1.0
 */
@SpringBootApplication
@EnableSwagger2
@RestController
@EnableAuthorizationServer
public class AppDemo {

    public static void main(String[] args) {
        SpringApplication.run(AppDemo.class, args);
    }

    @GetMapping("/hi")
    public String hello() {
        return "Hello, app";
    }

}
