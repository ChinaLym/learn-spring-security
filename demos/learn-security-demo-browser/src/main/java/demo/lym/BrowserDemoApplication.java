package demo.lym;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * demo
 *
 * @author lym
 * @since 1.0
 */
@SpringBootApplication
@EnableSwagger2
@RestController
public class BrowserDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BrowserDemoApplication.class, args);
    }

    @GetMapping("/hi")
    public String hello(){
        return "Hello, browser";
    }

}
