package com.zea.cloud.common.interceptor;

import cn.hutool.json.JSONUtil;
import com.zea.cloud.common.exception.ErrorCode;
import com.zea.cloud.common.utils.ResultUtil;
import com.zea.cloud.common.utils.ThreadLocalUtil;
import com.zea.cloud.common.utils.UserContextUtil;
import com.zea.cloud.tool.constant.HttpConstant;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;


@Component
@Slf4j
public class AuthRequestInterceptor implements HandlerInterceptor {

    @Value("${auth.enable}")
    private boolean authEnable;

    @Value("${no-filter.url}")
    private String noFilter;

    /**
     * 校验request请求头中的token对应的登录用户session是否过期
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 将 request 缓存到 ThreadLocal 中
        ThreadLocalUtil.set(ThreadLocalUtil.HTTP_SERVLET_REQUEST, request);

        // 是否启用用户登录验证
        if (!authEnable) {
            return true;
        }

        // 无需登录验证的url路径
        if (noFilter.contains(request.getRequestURI())) {
            return true;
        }

        // 如果请求头含有 HttpConstant.URL_FROM 字段且值为 HttpConstant.OPENFEIGN 则不拦截
        if(request.getHeader(HttpConstant.URL_FROM) != null
                && request.getHeader(HttpConstant.URL_FROM).equals(HttpConstant.OPENFEIGN)){
            return true;
        }

        try {
            // 校验request请求头中的token对应的登录用户session是否过期
            UserContextUtil.getCurrentUserInfo();
            // 未过期，刷新session，延时两小时
            UserContextUtil.refreshSession();
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            PrintWriter writer = response.getWriter();
            writer.write(JSONUtil.toJsonStr(ResultUtil.fail(ErrorCode.UN_AUTH)));
        }
        return false;
    }

}
