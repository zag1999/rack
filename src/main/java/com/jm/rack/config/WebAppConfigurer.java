package com.jm.rack.config;

import com.jm.rack.interceptor.LoginCheckHandlerInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import java.nio.charset.StandardCharsets;

@Configuration
public class WebAppConfigurer implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
     // registry.addInterceptor(new LoginCheckHandlerInterceptor());
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper helper = new UrlPathHelper();
        helper.setUrlDecode(false);
        helper.setDefaultEncoding(StandardCharsets.UTF_8.name());
        configurer.setUrlPathHelper(helper);
    }

}
