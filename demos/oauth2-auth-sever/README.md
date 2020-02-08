# 授权服务器 demo

演示如何提供一个 oauth2.0 的授权服务器

## 技术、版本选型

目前最新版的 `spring security oauth 2.3.x`

由于授权服务器在当前（2020.2）的spring security 最新版还未完成对应的功能，因此选择 spring security oauth来实现。

spring至少至少会维护该项目2.3.x至少到2021年3月以后，且其最后一个维护版本将是2.4.x，因此不必担心。



## 说明

默认值（`UserConfig#userDetailsService` 配置的）：
用户名：user
密码：password

默认的端信息（`AuthorizationServerConfiguration#configure(ClientDetailsServiceConfigurer)` 配置的）
clientId：demo
clientSecret: secret

#### 一个关键的方法
该方法作为spring security oauth2默认的处理认证请求的方法 `/oauth/authorize`
```
org.springframework.security.oauth2.provider.endpoint.AuthorizationEndpoint.authorize()
```

## 常见错误 

#### [authorization_request_not_found]
- 原因：
    授权服务器和请求的客户端域名相同，导致浏览器在请求 auth-server 时覆盖了 client 写入的 JessionId，导致认证完成后，再访问 client 时，会话失效

解决：
在 host 文件中设置
windows: 系统盘（一般为C）`c:\windows\system32\drivers\etc`
加入以下
```
127.0.0.1	authServer.com
127.0.0.1	resourceServer.com
```
