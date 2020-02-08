# spring security oauth2 介绍、使用、扩展

[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2)

[oauth2.0 协议文档](https://tools.ietf.org/html/rfc6749)

[如何与 spring boot 结合](https://spring.io/guides/topicals/spring-security-architecture/)

[OSChina 的 Spring Security OAuth2 开发指南](https://www.oschina.net/translate/spring-security-oauth-docs-oauth2)

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
        - 将oauth2 的认证请求重定向到授权服务器（授权码模式、简化模式专有）
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

### oauth2 协议中的名词简单介绍

OAuth2 是一种授权协议，而不是认证协议，因此它不能实现用户的认证。

- 认证和授权的区别：
    - 认证：用来识别访问者是谁
        - 用户名密码登录
        - 手机短信登录
        - 进车站要刷脸
    - 授权：用于控制访问者有无权限
        - A应用想使用B应用的api，需要B应用的用户对A应用授权。
        - 小区住户授予外卖小哥进入本楼道的权限
        
4个角色简介

- 客户端
    - 如希望实现第三方登录，调用第三方接口，那么你的应用就是 oauth2 客户端的角色
- 资源服务器
    - 如希望资源被保护，只有带有指定授权服务器发放的 accessToken 才能访问
- 授权服务器
    - 作为一个给其他调用者（客户端）授权的服务
- 单点登录客户端
    - 如公司内部不同系统间，使用 oauth2 jwt协议进行登录认证，可以说单点登录客户端是一种特殊的 oauth2 客户端

这里只做简单介绍，更多详细介绍可以参见文档中的链接。

### 文档助读
 
[官方文档](https://docs.spring.io/spring-security/site/docs/5.2.1.RELEASE/reference/htmlsingle/#oauth2)关于 oauth2 的主要内容在 12 章

- 首先给了 [demo 的链接地址](https://github.com/spring-projects/spring-security/tree/5.2.1.RELEASE/samples/boot/oauth2login)

- 告诉我们有哪些配置可以配

- spring-boot auto-configuration 干了什么？如何写脚手架（二次开发框架）个性化。

    - 注册一个 `ClientRegistrationRepository` @Bean
        - 使得 spring 可以通过这个 bean 拿到授权服务器相关信息 `ClientRegistration`
    - 注册一个 `WebSecurityConfigurerAdapter` 激活关键的 oauth2Login()
        - 如：`http.authorizeRequests(authorizeRequests -> authorizeRequests .anyRequest().authenticated() ) .oauth2Login(withDefaults());`

- 不用 Spring Boot 2.x 的自动装配怎么来激活
除了做上面说的两件事外，还需要
    - 在 `OAuth2AuthorizedClientService` 中注册你的 `clientRegistrationRepository`
    - 在 `ClientRegistrationRepository` 中注册你的 `OAuth2AuthorizedClientService`

- 高级配置
    - 使用个性的配置替换 `.oauth2Login(withDefaults())` 中的 `withDefaults()` 方法

### 如何利用 Spring Security 提供的能力快速建立一个 oauth2 协议的 `客户端` | `资源服务器` | `认证中心` | `单点登录` ？
1. 激活 spring security 实现接口 `WebSecurityConfigurerAdapter`，使用`@EnableWebSecurity`标记位配置类，并激活安全相关配置
2. 在 `configure(HttpSecurity http)` 方法中调用对应的 DSL 方法

- 客户端
    - `http.oauth2Client()`
- 资源服务器
    - `http.oauth2ResourceServer()`
- 授权服务器
   - 在 spring security5.2.x 的版本中没有提供该能力
- 单点登录客户端
        - `http.oauth2Login()`    



    
### JWT 知识补充

[JWT 知识补充](jwt.md)

## 授权服务器需要什么？

- 管理保存允许的客户端信息(ClientDetailsService)
    - 默认保存在内存(InMemoryClientDetailsService)
    - 另外支持 JdbcClientDetailsService
- 管理保存发放的 oauth2 Token(accessToken) 的接口 `[AuthorizationServerTokenServices](https://docs.spring.io/spring-security/oauth/apidocs/org/springframework/security/oauth2/provider/token/AuthorizationServerTokenServices.html)`
    - 默认实现：DefaultTokenServices
    - 保存token：`TokenStore`
        - 默认保存在内存(InMemoryTokenStore)
        - 其他支持 
            - JdbcTokenStore
            -  用数据库存储时记得需要 @EnableTransactionManagement 来防止在多个客户端创建令牌时产生行数据冲突。还要注意示例架构有明确的主键（PRIMARY KEY）声明——这些在开发环境中也是必需的。
        - RedisTokenStore
            - 相对于 jdbc 更快一些且可以更方便的设置过期时间
        - JwtTokenStore、JwkTokenStore(内置JwtTokenStore)
            - JWT 的实现依赖一个 `JwtAccessTokenConverter`而且授权服务器和资源服务器要有相同的实现。默认情况下令牌会有签名，而资源服务器也要能验签
                所以它需要与授权服务器相同的对称（签名）密钥（共享密钥或对称密钥），或者，它需要一个与授权服务器所使用的私钥配对的公钥（验证密钥）（公私密钥或非对称密钥）。
             公钥（如果有的话）会由授权服务器通过 `/oauth/token_key` 提供，默认是 “deenyAll()” 来保护的。
             因此可以向 AuthorizationServerSecurityConfigurer  注入一个标准的 SpEL 表达式（比如 “permitAll()” 就可以，因为那是公钥）。
- 端点 endPoint
            
- 框架提供的 URL 路径
    - `/oauth/authorize`（获取授权端点）
    - `/oauth/token`（获取令牌端点）
    - `/oauth/confirm_access`（用户在这里发布授权批准）
    - `/oauth/error`（用于在授权服务器上渲染错误）
    - `/oauth/check_token`（供资源服务器用来解码访问令牌）
    - `/oauth/token_key`（当使用JWT令牌，供资源服务器获取授权服务器的公钥公钥便于于token验签）

注： 授权端点/oauth/authorize（或其映射替代）应该使用Spring Security进行保护，以便只有通过身份验证的用户才能访问。例如使用标准的 Spring Security WebSecurityConfigurer：
```java
   @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests().antMatchers("/login").permitAll().and()
        // default protection for all resources (including /oauth/authorize)
            .authorizeRequests()
                .anyRequest().hasRole("USER")
        // ... more configuration, e.g. for form login
    }            
```

## spring security 提供了 oauth2 哪些配置项
- 授权服务器相关配置
    - AuthorizationServerEndpointsConfigurer
    - AuthorizationServerSecurityConfigurer
- 资源服务器相关配置
    - ResourceServerSecurityConfigurer 


## 数据库表介绍

[spring security oauth2 数据库表解读](http://andaily.com/spring-oauth-server/db_table_description.html)
    
    
## [OIDC](https://openid.net/specs/openid-connect-core-1_0.html)    
基于OAuth2的下一代身份认证授权协议

OIDC(OpenID Connect), 下一代的身份认证授权协议; 当前发布版本1.0;

OIDC是基于OAuth2+OpenID整合的新的认证授权协议; OAuth2是一个授权(authorization)的开放协议, 在全世界得到广泛使用, 但在实际使用中,OAuth2只解决了授权问题, 没有实现认证部分,往往需要添加额外的API来实现认证; 而OpenID呢,是一个认证(authentication )的协议, 二者在实际使用过程中都有其局限性;

综合二者,即是OIDC; 通过OIDC,既能有OAUTH2的功能,也有OpenID的功能; 恰到好处…

OIDC将是替换(或升级)OAuth2, OpenID的不二选择..

OIDC在OAuth2的access_token的基础上增加了身份认证信息; 通过公钥私钥配合校验获取身份等其他信息—– 即idToken;

在OIDC协议的实现中, 其底层是基于OAuth2. 一些常用的库如: JWT(https://jwt.io/), JWS; OAuth2的实现如: Spring Security OAuth, Spring Security, OLTU.

更多信息可参考: http://openid.net/connect/

OIDC 1.0协议: http://openid.net/specs/openid-connect-core-1_0.html