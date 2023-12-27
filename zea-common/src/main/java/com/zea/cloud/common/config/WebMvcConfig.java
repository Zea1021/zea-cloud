package com.zea.cloud.common.config;

import com.zea.cloud.common.interceptor.ForbidRepeatClickInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Resource
//    private RequestHeaderInterceptor requestHeaderInterceptor;
//
//    @Resource
//    private AuthRequestInterceptor authRequestInterceptor;

    @Resource
    private ForbidRepeatClickInterceptor forbidRepeatClickInterceptor;

    @Value("${nofilter.url}")
    private String noFilters;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(requestHeaderInterceptor).
//                addPathPatterns("/**");
//
//        registry.addInterceptor(authRequestInterceptor).
//                addPathPatterns("/**").excludePathPatterns(Arrays.asList(noFilters.split(",")));

        // 禁止重复点击的功能已经通过切面类实现，这里将拦截器注释掉防止同时起作用，导致每次都是重复点击
//        registry.addInterceptor(forbidRepeatClickInterceptor).addPathPatterns("/**");
    }

}
