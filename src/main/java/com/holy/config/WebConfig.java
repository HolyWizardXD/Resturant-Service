package com.holy.config;

import com.holy.interceptors.LoginInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LoginInterceptor loginInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 不经过token拦截器放行的地址
        registry.addInterceptor(loginInterceptor).
                excludePathPatterns(
                        "/user/login",
                        "/user/register",
                        "/customer/login",
                        "/customer/register",
                        "/v2/**",
                        "/v3/**",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/doc.html"
                );
    }
}
