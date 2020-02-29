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
package sample.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import sample.dto.DemoUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @author lym
 */
@RestController
public class DemoResourceController {

	Random r = new Random();

	@GetMapping("/")
	public String index(@AuthenticationPrincipal Jwt jwt) {
		return String.format("Hello, %s!", jwt.getSubject());
	}

	@GetMapping("/message")
	public String message() {
		return "Message";
	}

	@GetMapping("/messages")
	public List<String> messages() {
		int size = r.nextInt(10) + 5;
		List<String> list = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			list.add("message " + i + " : " + UUID.randomUUID());
		}
		return list;
	}

	@PostMapping("/message")
	public String createMessage(@RequestBody String message) {
		return String.format("Message was created. Content: %s", message);
	}

	/** 测试用户信息是否能传递拿到 */
	@GetMapping("/user")
	public DemoUser user() {
		Authentication authToken = SecurityContextHolder.getContext().getAuthentication();
		return new DemoUser();
	}

	/** 不登录也能访问 */
	@GetMapping("/ping")
	public String ping() {
		return "PONG";
	}

}
