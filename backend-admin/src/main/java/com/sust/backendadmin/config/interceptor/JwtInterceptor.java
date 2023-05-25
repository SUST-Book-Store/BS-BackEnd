package com.sust.backendadmin.config.interceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sust.backendadmin.utils.UserTokenUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

//token拦截器
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader("token");//从http请求头中获取token
        // 如果不是映射到方法直接通过
        if(!(handler instanceof HandlerMethod) || (token != null && UserTokenUtil.ValidateUserToken(token))){
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }
    }
}