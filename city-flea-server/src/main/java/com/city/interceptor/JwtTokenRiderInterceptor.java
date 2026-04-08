package com.city.interceptor;

import com.city.constant.JwtClaimsConstant;
import com.city.context.BaseContext;
import com.city.properties.JwtProperties;
import com.city.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenRiderInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        String token = request.getHeader(jwtProperties.getRiderTokenName());
        try {
            log.info("rider jwt check:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getRiderSecretKey(), token);
            Long riderId = Long.valueOf(claims.get(JwtClaimsConstant.RIDER_ID).toString());
            BaseContext.setCurrentId(riderId);
            return true;
        } catch (Exception ex) {
            response.setStatus(401);
            return false;
        }
    }
}