package com.zea.cloud.common.config;

import com.zea.cloud.common.interceptor.ForbidRepeatClickInterceptor;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

//    @Autowired
//    private RequestHeaderInterceptor requestHeaderInterceptor;
//
//    @Autowired
//    private AuthRequestInterceptor authRequestInterceptor;

    @Resource
    private ForbidRepeatClickInterceptor forbidRepeatClickInterceptor;

//    @Value("${nofilter.url}")
//    private String noFilters;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(requestHeaderInterceptor).
//                addPathPatterns("/**");
//
//        registry.addInterceptor(authRequestInterceptor).
//                addPathPatterns("/**").excludePathPatterns(Arrays.asList(noFilters.split(",")));

//        registry.addInterceptor(forbidRepeatClickInterceptor).addPathPatterns("/**");
    }

//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver() {
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
//        resolver.setMaxInMemorySize(40960);
//        resolver.setMaxUploadSize(200*1024*1024);//上传文件大小 50M 50*1024*1024
//        return resolver;
//    }

    //    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver(){
//        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
//        resolver.setDefaultEncoding("UTF-8");
//        resolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
//        resolver.setMaxInMemorySize(40960);
//        resolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
//        return resolver;
//    }
}
