package com.lym.security.code.processor;

import com.lym.security.code.dto.ValidateCodeDTO;
import com.lym.security.code.exception.ValidateCodeAuthenticationException;
import com.lym.security.code.exception.ValidateCodeException;
import com.lym.security.code.generator.ValidateCodeGenerator;
import com.lym.security.code.propertities.ValidateCodeProperties;
import com.lym.security.code.store.ValidateCodeStore;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * 基础的验证码处理器
 *
 * @param <C> 验证码 code
 */
public abstract class AbstractValidateCodeProcessor<C extends ValidateCodeDTO> implements ValidateCodeProcessor {

    protected ValidateCodeProperties validateCodeProperties;

    protected ValidateCodeGenerator validateCodeGenerator;

    protected ValidateCodeStore validateCodeStore;


    public AbstractValidateCodeProcessor(@Nullable ValidateCodeProperties validateCodeProperties,
                                         ValidateCodeGenerator validateCodeGenerator,
                                         ValidateCodeStore validateCodeStore) {
        this.validateCodeProperties = validateCodeProperties;
        this.validateCodeGenerator = validateCodeGenerator;
        this.validateCodeStore = validateCodeStore;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void create(ServletWebRequest request) throws ValidateCodeException {
        if (isPostOnly() && HttpMethod.POST != request.getHttpMethod()) {
            final String errorMsg = "such type of validateCode only support POST.";
            try {
                Objects.requireNonNull(request.getResponse()).sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, errorMsg);
            } catch (Exception e) {
                throw new ValidateCodeException(errorMsg, e);
            }
        }
        // 生成校验码
        C validateCode = (C) validateCodeGenerator.generate(request);
        save(request, validateCode);
        send(request, validateCode);
    }

    /**
     * 保存校验码
     */
    private void save(ServletWebRequest request, C validateCode) {
        ValidateCodeDTO code = new ValidateCodeDTO(validateCode.getCode(), validateCode.getExpireTime());
        validateCodeStore.save(request, code, getType());
    }

    /**
     * 发送校验码
     *
     * @param request      本次请求和响应
     * @param validateCode 生成好的验证码
     */
    public abstract void send(ServletWebRequest request, C validateCode) throws ValidateCodeException;

    /**
     * 是否只允许 post 请求才能获取验证码
     */
    protected boolean isPostOnly() {
        return false;
    }


    @SuppressWarnings("unchecked")
    @Override
    public void validate(ServletWebRequest request) {

        String codeType = getType();

        String codeInRequest;
        try {
            codeInRequest = ServletRequestUtils.getStringParameter(request.getRequest(),
                    validateCodeProperties.getParameterName());
        } catch (ServletRequestBindingException e) {
            // 未能正确从请求中获取验证码
            throw new ValidateCodeAuthenticationException("cant get validate-code(" + codeType + ") from request.", e);
        }

        // 验证码的值不能为空
        if (StringUtils.isBlank(codeInRequest)) {
            throw new ValidateCodeAuthenticationException(codeType + "validate-code is empty(" + codeType + ")");
        }

        // 期望的值
        C exceptCode = (C) validateCodeStore.get(request, codeType);

        // 验证码不存在 或 已过期
        if (exceptCode == null || exceptCode.isExpire()) {
            validateCodeStore.remove(request, codeType);
            throw new ValidateCodeAuthenticationException(codeType + "validate-code is expire, please retry!");
        }

        // 验证码不匹配（验证码不正确）
        if (!StringUtils.equalsIgnoreCase(exceptCode.getCode(), codeInRequest)) {
            throw new ValidateCodeAuthenticationException(codeType + "validate-code not correct!");
        }

        validateCodeStore.remove(request, codeType);

    }

    @Override
    public List<String> processedUrls() {
        return validateCodeProperties.getUrls();
    }

}