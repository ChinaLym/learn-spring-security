# 面向浏览器额外的支持

当后端服务器是面向浏览器的 web 程序时候（如浏览器直接访问tomcat），引入这个包

对浏览器的额外支持如下：
- 基本的认证页面（登录页面）
- Session 的无效、过期策略
- 认证成功、失败、退出登录的处理
- 默认的待认证请求处理
- 对验证码进行默认配置

且实现了自动完成 spring security 相关的配置、依赖的 bean 注入等，达到开箱即用的效果

以上部分均支持使用者自行替换，如自定义各种页面（如登录、注册、退出等）、各类请求url、请求页面参数、会话过期时间等

---

- 为什么 使用 spring 不推荐的 APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8" ？
    - 为了支持旧版本的浏览器

- 启动后报错：`org.springframework.security.web.authentication.rememberme.CookieTheftException: Invalid remember-me token (Series/token) mismatch. Implies previous cookie theft attack.`
    - 一般是极端时间内发送了两个请求导致的:第一个请求将通过，并为随后的请求生成新的哈希，但第二个请求仍带着旧的令牌来访问，因此导致异常。
    - 解决：静态资源不要拦截，如 css、jpg、js 等，保证请求一个页面时仅发生一次过滤即可。其他：该情况经常在启动后第一次访问，连续刷新页面时发生。