package com.holy.interceptors;

import com.holy.utils.JwtUtil;
import com.holy.utils.ThreadLocalUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 令牌验证
        // 1.从请求头中获取token
        String token = request.getHeader("authorization");
        // 2.校验token
        try {
            Map<String, Object> claims = JwtUtil.parseToken(token);
            // 3.数据存入ThreadLocal
            ThreadLocalUtil.set(claims);
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 清空ThreadLocal
        ThreadLocalUtil.remove();
    }


}
