package com.city.interceptor;

import com.city.constant.JwtClaimsConstant;
import com.city.context.BaseContext;
import com.city.exception.BaseException;
import com.city.properties.JwtProperties;
import com.city.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.city.properties.DeliveryProperties;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class JwtTokenRiderInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private DeliveryProperties deliveryProperties;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (Boolean.FALSE.equals(deliveryProperties.getRiderEnabled())) {
            log.warn("骑手模块已禁用");
            if (!(handler instanceof HandlerMethod)) {
                response.setStatus(403);
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":403,\"msg\":\"骑手配送模式已停用，当前系统使用快递物流模式\"}");
                return false;
            }
            throw new BaseException("骑手配送模式已停用，当前系统使用快递物流模式");
        }

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
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
