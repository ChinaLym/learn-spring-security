package demo.lym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 认证服务器
 * @author lym
 * @since 1.0
 */
@SpringBootApplication
public class OAuth2AuthServer {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2AuthServer.class, args);
    }
}
