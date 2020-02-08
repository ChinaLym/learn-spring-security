/*
 * Copyright 2002-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author lym
 */
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class OAuth2ResourceServerSecurityConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				//.mvcMatcher("/messages/**")
					.authorizeRequests()
						//.mvcMatchers("/messages/**").access("hasAuthority('SCOPE_message.read')")
						.antMatchers(HttpMethod.GET, "/message/**").hasAuthority("SCOPE_message:read")
						.antMatchers(HttpMethod.POST, "/message/**").hasAuthority("SCOPE_message:write")
						.antMatchers(HttpMethod.GET, "/user").hasAuthority("SCOPE_user:read")
						.antMatchers("/ping").permitAll()
				.anyRequest().authenticated()

				.and()
					.oauth2ResourceServer()
						.jwt();
	}
	// @formatter:on
    // 这里遵循 spring security 的配置项进行配置，让 spring 注入 jwk Decoder
    // 如本 demo 通过配置 spring.security.oauth2.resourceserver.jwt.jwk-set-uri 来提供
}
