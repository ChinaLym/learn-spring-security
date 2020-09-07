package com.lym.security.code.generator;

import com.lym.security.code.ValidateCodeType;
import com.lym.security.code.dto.ValidateCodeDTO;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 验证码生成器
 *
 * @author lym
 */
public interface ValidateCodeGenerator extends ValidateCodeType {

    ValidateCodeDTO generate(ServletWebRequest request);

}
