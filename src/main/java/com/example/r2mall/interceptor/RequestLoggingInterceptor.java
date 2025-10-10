package com.example.r2mall.interceptor;

import cn.dev33.satoken.stp.StpUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Enumeration;

/**
 * 请求日志拦截器
 */
@Slf4j
@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = System.currentTimeMillis();
        request.setAttribute("startTime", startTime);

        // 记录请求信息
        log.info("=== 请求开始 ===");
        log.info("请求URL: {} {}", request.getMethod(), request.getRequestURI());
        log.info("请求IP: {}", getClientIpAddress(request));
        log.info("User-Agent: {}", request.getHeader("User-Agent"));
        
        // 记录请求参数
        logRequestParameters(request);
        
        // 记录用户信息（如果已登录）
        try {
            if (StpUtil.isLogin()) {
                Long userId = StpUtil.getLoginIdAsLong();
                log.info("当前用户ID: {}", userId);
            }
        } catch (Exception e) {
            // 忽略登录检查异常
        }

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        long startTime = (Long) request.getAttribute("startTime");
        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        log.info("响应状态: {}", response.getStatus());
        log.info("请求耗时: {}ms", duration);
        
        if (ex != null) {
            log.error("请求处理异常: {}", ex.getMessage(), ex);
        }
        
        log.info("=== 请求结束 ===");
    }

    /**
     * 记录请求参数
     */
    private void logRequestParameters(HttpServletRequest request) {
        try {
            // 记录查询参数
            String queryString = request.getQueryString();
            if (queryString != null && !queryString.isEmpty()) {
                log.info("查询参数: {}", queryString);
            }

            // 记录请求头
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String headerName = headerNames.nextElement();
                if (isImportantHeader(headerName)) {
                    log.info("请求头 {}: {}", headerName, request.getHeader(headerName));
                }
            }
        } catch (Exception e) {
            log.warn("记录请求参数时发生异常: {}", e.getMessage());
        }
    }

    /**
     * 判断是否为重要的请求头
     */
    private boolean isImportantHeader(String headerName) {
        return "Authorization".equalsIgnoreCase(headerName) ||
               "Content-Type".equalsIgnoreCase(headerName) ||
               "Accept".equalsIgnoreCase(headerName) ||
               "X-Forwarded-For".equalsIgnoreCase(headerName) ||
               "X-Real-IP".equalsIgnoreCase(headerName);
    }

    /**
     * 获取客户端真实IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
