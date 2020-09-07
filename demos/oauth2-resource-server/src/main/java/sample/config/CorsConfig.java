package sample.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 跨域请求配置
 */
@Configuration
public class CorsConfig extends WebMvcConfigurerAdapter {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedOrigins("*")
                .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type",
                        "Last-Modified", "device", "token", "x-token")
                .exposedHeaders("Set-Cookie")
                .allowCredentials(true).maxAge(3600);
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        // 对响应头进行CORS授权
        MyCorsRegistration corsRegistration = new MyCorsRegistration("/**");
        corsRegistration.allowedOrigins("*")
                .allowedMethods(HttpMethod.GET.name(), HttpMethod.HEAD.name(), HttpMethod.POST.name(),
                        HttpMethod.PUT.name(), HttpMethod.OPTIONS.name())
                .allowedHeaders("Accept", "Origin", "X-Requested-With", "Content-Type",
                        "Last-Modified", "device", "token", "x-token")
                .exposedHeaders(HttpHeaders.SET_COOKIE)
                .allowCredentials(true)
                .maxAge(3600);

        // 注册CORS过滤器
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        configurationSource.registerCorsConfiguration("/**", corsRegistration.getCorsConfiguration());
        CorsFilter corsFilter = new CorsFilter(configurationSource);
        return new FilterRegistrationBean(corsFilter);
    }

    public class MyCorsRegistration extends CorsRegistration {

        public MyCorsRegistration(String pathPattern) {
            super(pathPattern);
        }

        /**
         * 开放该方法访问权限为 public
         */
        @Override
        public CorsConfiguration getCorsConfiguration() {
            return super.getCorsConfiguration();
        }
    }
}