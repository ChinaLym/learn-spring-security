# 基础定义

- 认证（登录） Oauth2.0
    - 模式：采取主流、功能最完善的 授权码模式

- 授权


响应的 JSON 推荐为
```json
{
"code":"int",
"data":"object",
"msg":"string"
}
```
本框架仅依赖 msg 字段，即默认的响应为
```json
{
"msg":"xxx"
}
```