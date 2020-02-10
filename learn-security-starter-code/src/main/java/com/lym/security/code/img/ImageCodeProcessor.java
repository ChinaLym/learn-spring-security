package com.lym.security.code.img;

import com.lym.security.code.exception.ValidateCodeException;
import com.lym.security.code.generator.ValidateCodeGenerator;
import com.lym.security.code.processor.AbstractValidateCodeProcessor;
import com.lym.security.code.propertities.ValidateCodeProperties;
import com.lym.security.code.store.ValidateCodeStore;
import org.springframework.web.context.request.ServletWebRequest;

import javax.imageio.ImageIO;
import java.util.Objects;

/**
 * 图片验证码处理器
 * @author lym
 */
public class ImageCodeProcessor extends AbstractValidateCodeProcessor<ImageCode> implements ImageValidateCodeType {

	public ImageCodeProcessor(ValidateCodeProperties validateCodeProperties, ValidateCodeGenerator validateCodeGenerator, ValidateCodeStore validateCodeStore) {
		super(validateCodeProperties, validateCodeGenerator, validateCodeStore);
	}

	/**
	 * 发送图形验证码，将其写到响应中
	 */
	public void send(ServletWebRequest request, ImageCode imageCode) throws ValidateCodeException {
		try{
			ImageIO.write(imageCode.getImage(), "JPEG", Objects.requireNonNull(request.getResponse()).getOutputStream());
		}catch(Exception e){
			throw new ValidateCodeException("send validate code fail.", e);
		}
	}

}
