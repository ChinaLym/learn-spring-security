package com.lym.security.code.store;

import com.lym.security.code.dto.ValidateCodeDTO;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * 校验码存储
 *
 * @author lym
 */
public interface ValidateCodeStore {

    /**
     * 保存
     */
    void save(ServletWebRequest request, ValidateCodeDTO code, String validateCodeType);

    /**
     * 获取
     */
    ValidateCodeDTO get(ServletWebRequest request, String validateCodeType);

    /**
     * 移除
     */
    void remove(ServletWebRequest request, String codeType);

}
