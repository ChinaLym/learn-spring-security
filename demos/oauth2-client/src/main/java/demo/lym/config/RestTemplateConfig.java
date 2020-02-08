package demo.lym.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * provider a restTemplate Bean
 * @author lym
 */
@Configuration
public class RestTemplateConfig {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		RestTemplate rest = restTemplateBuilder.build();

		//替换默认转换器
		List<HttpMessageConverter<?>> messageConverters = rest.getMessageConverters();
		if(messageConverters == null){
			messageConverters = new ArrayList<>();
		}
		messageConverters.set(1,restStringConverter());

		boolean existFlag = false;
		for (int i = 0; i < messageConverters.size(); i++) {

			HttpMessageConverter<?> messageConverter = messageConverters.get(i);
			if (messageConverter instanceof MappingJackson2HttpMessageConverter){
				existFlag = true;
				messageConverters.set(i,restMappingJackson2HttpMessageConverter());
			}
		}
		if(!existFlag){
			messageConverters.add(restMappingJackson2HttpMessageConverter());
		}

		messageConverters.add(new OAuth2AccessTokenResponseHttpMessageConverter());
		rest.setMessageConverters(messageConverters);

		rest.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		// 如果当前上下文中有 token 则放头部 实现传递
		rest.getInterceptors().add((request, body, execution) -> {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			boolean noAuth = authentication == null;
			if (noAuth) {
				return execution.execute(request, body);
			}
			boolean notOAuth2Token = !(authentication.getCredentials() instanceof AbstractOAuth2Token);
			if (notOAuth2Token) {
				return execution.execute(request, body);
			}

			AbstractOAuth2Token token = (AbstractOAuth2Token) authentication.getCredentials();
			request.getHeaders().setBearerAuth(token.getTokenValue());
			return execution.execute(request, body);
		});

		return rest;
	}

	@Bean
	public MappingJackson2HttpMessageConverter restMappingJackson2HttpMessageConverter(){

		MappingJackson2HttpMessageConverter messageConverter = new MappingJackson2HttpMessageConverter();
		ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder().build();
		DateFormat df = new StdDateFormat();
		objectMapper.setDateFormat(df);
		messageConverter.setObjectMapper(objectMapper);

		List<MediaType> list = new ArrayList<>(messageConverter.getSupportedMediaTypes());
		list.add(new MediaType("text","json", Charset.forName("UTF-8")));
		list.add(new MediaType("application","octet-stream",Charset.forName("UTF-8")));
		list.add(MediaType.APPLICATION_OCTET_STREAM);
		messageConverter.setSupportedMediaTypes(list);
		return messageConverter;
	}

	@Bean
	public StringHttpMessageConverter restStringConverter(){
		StringHttpMessageConverter stringConvert = new StringHttpMessageConverter(Charset.forName("UTF-8"));
		stringConvert.setWriteAcceptCharset(false);

		List<MediaType> list = new ArrayList<MediaType>(){{
			addAll(stringConvert.getSupportedMediaTypes());
		}};
		list.add(0,new MediaType("text","plain",Charset.forName("UTF-8")));
		stringConvert.setSupportedMediaTypes(list);
		return stringConvert;
	}

}
