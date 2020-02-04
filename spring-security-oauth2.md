# spring security oauth2 介绍、使用、扩展

[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2)

[oauth2.0 协议文档](https://tools.ietf.org/html/rfc6749)

[如何与 spring boot 结合](https://spring.io/guides/topicals/spring-security-architecture/)

## spring security 介绍

一句话介绍：spring security 原理其实是内置了多个过滤器，在请求到达时先经过 spring security 的 filter，如果 filter 允许访问则通过。

#### 过滤器链介绍
    
- 通用过滤器
    - WebAsyncManagerIntegrationFilter
        - 通过`SecurityContextCallableProcessingInterceptor` 的 
          `beforeConcurrentHandling(NativeWebRequest, Callable)` 方法填充 `Callable` 中的 `SecurityContext`.    
          在 `SecurityContext` 和 Spring Web's `WebAsyncManager` 之间提供继承
          
    - SecurityContextPersistenceFilter
        - 如果是之前登录过的，则session会关联上登录用户信息，包含用户的AuthenticationT信息，比如Principal，Granted Authorities等，这些都是重要的鉴权依据。
          如果取出来没有的话，则new一个
          
    - HeaderWriterFilter
        - 用于增加一些浏览器安全headers，比如X-Frame-Options, X-XSS-Protection和X-Content-Type-Options等。
        
    - LogoutFilter
        - 如果是退出登录请求(默认`/logout/cas`)，则将Session失效、将SecurityContextHolder清空、调用`logoutHandler`

    - 可选的一些过滤器
        - DefaultLoginPageGeneratingFilter
            - 没有配置登录页面时，Spring Security Web会自动构造一个登录页面,form|openId
        - DefaultLogoutPageGeneratingFilter
            - 缺省的用户退出登录页面(GET /logout)
                
    - RequestCacheAwareFilter
        - 默认的`RequestCache`(默认`HttpSessionRequestCache`)，将请求保存在http session中
        必须是 GET /**
        并且不能是 /**/favicon.*
        并且不能是 application.json
        并且不能是 XMLHttpRequest (也就是一般意义上的 ajax 请求)
        
    - SecurityContextHolderAwareRequestFilter
        - 将请求包装成一个可以访问`SecurityContextHolder`中安全上下文的`SecurityContextHolderAwareRequestWrapper`。
          这样接口`HttpServletRequest上`定义的`getUserPrincipal`这种安全相关的方法才能访问到相应的安全信息。
    - AnonymousAuthenticationFilter
        - 匿名用户认证过滤器，如果在此之前没有被认证，则被认证为匿名用户
        
    - SessionManagementFilter
        - 检测从当前请求处理开始到目前为止的过程中是否发生了用户登录认证行为(比如这是一个用户名/密码表单提交的请求处理过程)，
          如果检测到这一情况，执行相应的session认证策略(`SessionAuthenticationStrategy`)
          
    - ExceptionTranslationFilter
        - 处理转换 `FilterSecurityInterceptor` 抛出的异常
        
    - FilterSecurityInterceptor
        - 判断认证、权限校验的最后一环
        
独特的已加粗
    
- Security filter chain
    - WebAsyncManagerIntegrationFilter
    - SecurityContextPersistenceFilter
    - HeaderWriterFilter
    - LogoutFilter
    - **OAuth2AuthenticationProcessingFilter**
        - OAuth2 预认证处理器，从请求头部取出 oauth2 token（`OAuth2Authentication`），
        然后调用 `OAuth2AuthenticationManager` 去处理，
        如果有效，则放入`SecurityContext` 中，表示已登录
    - **UsernamePasswordAuthenticationFilter**
        - 用户名密码表单登录 `POST /login`
    - DefaultLoginPageGeneratingFilter
    - DefaultLogoutPageGeneratingFilter
    - **BasicAuthenticationFilter**
        - 基本认证方式：即弹出一个框，输入user和password，允许在url上认证
    - RequestCacheAwareFilter
    - SecurityContextHolderAwareRequestFilter
    - AnonymousAuthenticationFilter
    - SessionManagementFilter
    - ExceptionTranslationFilter
    - FilterSecurityInterceptor

- OAuth2Client Filter链
    - WebAsyncManagerIntegrationFilter
    - SecurityContextPersistenceFilter
    - HeaderWriterFilter
    - **CsrfFilter**
        - 防御 csrf 的过滤器，在请求时会检测头部是否携带 _csrf_token
    - LogoutFilter
    - **OAuth2AuthorizationRequestRedirectFilter**
        - 将oauth2 的认证请求重定向到认证服务器（授权码模式、简化模式专有）
    - **OAuth2LoginAuthenticationFilter**
        - `AbstractAuthenticationProcessingFilter` 的一个实现，负责处理oauth认证
    - **DefaultLoginPageGeneratingFilter**
        - 提供默认的oauth2.0登录页面
    - **DefaultLogoutPageGeneratingFilter**
        - 提供默认的oauth2.0的退出页面
    - RequestCacheAwareFilter
    - SecurityContextHolderAwareRequestFilter
    - AnonymousAuthenticationFilter
    - SessionManagementFilter
    - ExceptionTranslationFilter
    - FilterSecurityInterceptor

## 
```java
@RequestMapping("/foo")
public String foo(@AuthenticationPrincipal User user) {
  ... // do stuff with user
}
```
在controller的requestMapping方法中，参数加 `@AuthenticationPrincipal` 相当于 `SecurityContextHolder.getContext().getAuthentication().getPrincipal();`
等效于：
```java
@RequestMapping("/foo")
public String foo(Principal principal) {
  Authentication authentication = (Authentication) principal;
  User = (User) authentication.getPrincipal();
  ... // do stuff with user
}
```

## spring security oauth2 
[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2)关于 oauth2 的主要内容在 12 章

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
    