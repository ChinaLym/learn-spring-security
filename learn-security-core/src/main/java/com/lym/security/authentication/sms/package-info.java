/**
 * 扩展 spring security 认证方式，提供手机号码认证
 * 该 api 只输入手机号码就能登录，因此需要设置 安全过滤器来保护，如 code-starter 包中提供了 手机短信验证码 的方式对其保护
 * 实际中可以自由添加自定义的方式，如既可以手机号+短信验证码登录、也可以手机号+密码登录，还可以手机号+指纹，手机号+人脸登录
 *
 *
 * @author lym
 * @since 1.0
 */
package com.lym.security.authentication.sms;