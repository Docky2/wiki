package com.docky.wiki.config;


import com.docky.wiki.interceptor.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    LoginInterceptor loginInterceptor;



    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                // 拦截所有除以下请求的请求
                .addPathPatterns("/**").excludePathPatterns(
                "/test/**",
                "/redis/**",
                "/user/login",
                "/category/all",
                "/ebook/list",
                "/doc/all/**",
                "/doc/list/**",
                "/doc/vote/**",
                "/doc/find-content/**"
        );
    }
}
