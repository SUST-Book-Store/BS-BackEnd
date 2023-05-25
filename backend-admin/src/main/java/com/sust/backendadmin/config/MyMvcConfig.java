package com.sust.backendadmin.config;


import com.sust.backendadmin.config.interceptor.JwtInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//注册拦截器
@Configuration
public class MyMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求，通过判断token是否合法来决定是否需要登录
                .excludePathPatterns("/user/login", "/user/register");
//                        "/file/**",
//                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/api", "/api-docs", "/api-docs/**")
//                .excludePathPatterns( "/**/*.html", "/**/*.js", "/**/*.css", "/**/*.woff", "/**/*.ttf");
// 放行静态文件
    }

    //将对象注入到容器中
    @Bean
    public JwtInterceptor jwtInterceptor() {
        return new JwtInterceptor();
    }

}