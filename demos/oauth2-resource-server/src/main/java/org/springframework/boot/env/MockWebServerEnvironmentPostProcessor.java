/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.boot.env;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * 本demo中默认使用 authServer.com:8081 作为授权服务器，mockAuthServer 其实不需要了
 * @author Rob Winch
 */
@ConditionalOnProperty(prefix = "test", havingValue = "true")
@Configuration
public class MockWebServerEnvironmentPostProcessor
		implements EnvironmentPostProcessor, DisposableBean {

	private final MockWebServerPropertySource propertySource = new MockWebServerPropertySource();

	@Override
	public void postProcessEnvironment(ConfigurableEnvironment environment,
			SpringApplication application) {
		environment.getPropertySources().addFirst(this.propertySource);
	}

	@Override
	public void destroy() throws Exception {
		this.propertySource.destroy();
	}
}
