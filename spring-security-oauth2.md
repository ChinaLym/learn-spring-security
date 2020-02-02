# spring security oauth2 介绍、使用、扩展

[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2)

[oauth2.0 协议文档](https://tools.ietf.org/html/rfc6749)

spring security oauth2 的主要内容在 12 章

- 首先给了 [demo 的链接地址](https://github.com/spring-projects/spring-security/tree/5.2.1.RELEASE/samples/boot/oauth2login)

- 告诉我们有哪些配置可以配

- spring-boot auto-configuration 干了什么？如何写脚手架（二次开发框架）个性化。

    - 注册一个 `ClientRegistrationRepository` @Bean
        - 使得 spring 可以通过这个 bean 拿到认证服务器相关信息 `ClientRegistration`
    - 注册一个 `WebSecurityConfigurerAdapter` 激活关键的 oauth2Login()
        - 如：`http.authorizeRequests(authorizeRequests -> authorizeRequests .anyRequest().authenticated() ) .oauth2Login(withDefaults());`

- 不用 Spring Boot 2.x 的自动装配怎么来激活
除了做上面说的两件事外，还需要
    - 在 `OAuth2AuthorizedClientService` 中注册你的 `clientRegistrationRepository`
    - 在 `ClientRegistrationRepository` 中注册你的 `OAuth2AuthorizedClientService`

- 高级配置
    - 使用个性的配置替换 `.oauth2Login(withDefaults())` 中的 `withDefaults()` 方法
    