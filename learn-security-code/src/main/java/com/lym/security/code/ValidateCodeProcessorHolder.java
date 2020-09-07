package com.lym.security.code;

import com.lym.security.code.exception.NoSuchValidateCodeProcessorException;
import com.lym.security.code.exception.ValidateCodeAuthenticationException;
import com.lym.security.code.processor.ValidateCodeProcessor;
import org.springframework.beans.factory.InitializingBean;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提供处理器
 *
 * @author lym
 */
public class ValidateCodeProcessorHolder implements InitializingBean {

    /**
     * 所有的验证码处理器
     */
    private List<ValidateCodeProcessor> allProcessors;

    private Map<String, ValidateCodeProcessor> processorMap;

    public ValidateCodeProcessorHolder(List<ValidateCodeProcessor> allProcessors) {
        this.allProcessors = allProcessors;
    }

    /**
     * 获取验证码处理器
     *
     * @throws ValidateCodeAuthenticationException 没有对应的验证码处理器
     */
    public ValidateCodeProcessor getProcessor(String type) throws NoSuchValidateCodeProcessorException {
        ValidateCodeProcessor processor = processorMap.get(type);
        if (processor == null) {
            // "ValidateCodeProcessor(type) not exist."
            throw new NoSuchValidateCodeProcessorException("not support such validateCode(" + type + ")");
        }
        return processor;
    }


    @Override
    public void afterPropertiesSet() {
        processorMap = new HashMap<>(allProcessors.size());
        for (ValidateCodeProcessor processor : allProcessors) {
            processorMap.put(processor.getType(), processor);
        }
    }

    public List<ValidateCodeProcessor> getAllProcessors() {
        return allProcessors;
    }
}
