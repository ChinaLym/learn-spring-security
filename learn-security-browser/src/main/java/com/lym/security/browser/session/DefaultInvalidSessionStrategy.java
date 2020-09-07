package com.lym.security.browser.session;

import com.lym.security.browser.properties.BrowserProperties;
import org.springframework.security.web.session.InvalidSessionStrategy;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认的 session 无效处理策略
 * （第一次访问必定无效）
 *
 * @author lym
 */
public class DefaultInvalidSessionStrategy extends AbstractSessionStrategy implements InvalidSessionStrategy {

    public DefaultInvalidSessionStrategy(BrowserProperties browserProperties) {
        super(browserProperties);
    }

    @Override
    public void onInvalidSessionDetected(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        onSessionInvalid(request, response);
    }

}
