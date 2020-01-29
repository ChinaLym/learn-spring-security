package com.lym.security.code.store.impl;

import com.lym.security.code.dto.ValidateCodeDTO;
import com.lym.security.code.store.ValidateCodeStore;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 基于session的验证码存取器
 * @author lym
 */
public class SessionValidateCodeRepository implements ValidateCodeStore {

	/**
	 * 验证码放入session时的前缀
	 */
	private static final String SESSION_KEY_PREFIX = "LYM_VALIDATE_CODE";
	
	@Override
	public void save(ServletWebRequest request, ValidateCodeDTO code, String validateCodeType) {
		request.getRequest().getSession().setAttribute(builderSessionKey(validateCodeType), code);
	}
	

	@Override
	public ValidateCodeDTO get(ServletWebRequest request, String validateCodeType) {
		return (ValidateCodeDTO) request.getRequest().getSession().getAttribute(builderSessionKey(validateCodeType));
	}

	@Override
	public void remove(ServletWebRequest request, String codeType) {
		request.getRequest().getSession().removeAttribute(builderSessionKey(codeType));
	}

	/**
	 * 拼装验证码放入session时的key
	 */
	private String builderSessionKey(String validateCodeType) {
		return SESSION_KEY_PREFIX + validateCodeType.toUpperCase();
	}

}