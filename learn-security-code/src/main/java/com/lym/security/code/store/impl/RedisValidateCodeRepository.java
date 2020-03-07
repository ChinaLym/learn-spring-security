package com.lym.security.code.store.impl;

import com.lym.security.code.dto.ValidateCodeDTO;
import com.lym.security.code.exception.ValidateCodeException;
import com.lym.security.code.store.ValidateCodeStore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.context.request.ServletWebRequest;

import java.util.concurrent.TimeUnit;

/**
 * 基于redis的验证码存取器，避免由于没有session导致无法存取验证码的问题
 * @author lym
 */
public class RedisValidateCodeRepository implements ValidateCodeStore {

	private static final String DEFAULT_KEY_PREFIX = "CAPTCHA_CODE:";

	private RedisTemplate<Object, Object> redisTemplate;

	@SuppressWarnings("unchecked")
	public RedisValidateCodeRepository(RedisTemplate redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	@Override
	public void save(ServletWebRequest request, ValidateCodeDTO code, String type) {
		redisTemplate.opsForValue().set(buildKey(request, type), code, 30, TimeUnit.MINUTES);
	}


	@Override
	public ValidateCodeDTO get(ServletWebRequest request, String type) {
		Object value = redisTemplate.opsForValue().get(buildKey(request, type));
		if (value == null) {
			return null;
		}
		return (ValidateCodeDTO) value;
	}


	@Override
	public void remove(ServletWebRequest request, String type) {
		redisTemplate.delete(buildKey(request, type));
	}

	/**
	 * 返回在 redis 中存储验证码的 key
	 * @param request request response
	 * @param type 类型
	 * @return 在 redis 中存储验证码的 key
	 */
	protected String buildKey(ServletWebRequest request, String type) {
		String deviceId = request.getHeader("deviceId");
		if (StringUtils.isBlank(deviceId)) {
			throw new ValidateCodeException("please add the parameter deviceId in your requests!");
		}
		return DEFAULT_KEY_PREFIX + deviceId + ":" + type;
	}

}