package edu.simrobot;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Interceptor 配置类
 * 因为 exclude 路径一直没有配置好，所以暂弃
 * */

@Configuration
public class InterceptorConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new UrlInterceptor())
//                .addPathPatterns("/**")
//                .excludePathPatterns("/test/**","/**/login","/**/register","/**/logout");
    }
}
