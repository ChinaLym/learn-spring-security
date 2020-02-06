# oauth2 客户端 demo

演示 oauth2.0 协议的认证

该 demo 演示了以 oauth2.0 第三方登录（oauth-server认证） 

## 调试帮助

演示如何提供一个 oauth2.0 的认证服务器

启动后直接访问 `http://demo.com` 或 `http://localhost` 或 `http://127.0.0.1`

选择：demos里的 oauth2-auth-server

输入用户名密码

## 说明

application.yml 配置了使用授权码模式与 demo-auth-server 交互

#### [missing_user_info_uri] Missing required UserInfo Uri in UserInfoEndpoint for Client Registration: demo

没有设置 userInfo uri