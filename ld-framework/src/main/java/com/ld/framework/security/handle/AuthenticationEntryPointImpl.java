package com.ld.framework.security.handle;

import java.io.IOException;
import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.alibaba.fastjson2.JSON;
import com.ld.common.constant.HttpStatus;
import com.ld.common.core.domain.AjaxResult;
import com.ld.common.utils.ServletUtils;
import com.ld.common.utils.StringUtils;

/**
 * 认证失败处理类 返回未授权
 * 
 * @author wk
 */
@Slf4j
@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint, Serializable  {
    private static final long serialVersionUID = -8970718410437077606L;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) {
        int code = HttpStatus.UNAUTHORIZED;
        String msg = StringUtils.format("请求访问：{}，认证失败，无法访问系统资源", request.getRequestURI());
        log.error(msg);
        ServletUtils.renderString(response, code, JSON.toJSONString(AjaxResult.error(code, msg)));
    }
}
