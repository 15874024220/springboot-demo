package com.example.demo.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 *  案例1：请求响应日志过滤器
 */
@Slf4j
public class LoggingFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 包装请求和响应，支持多次读取 body
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(req);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(res);

        long startTime = System.currentTimeMillis();

        try {
//            chain.doFilter(req, res);
            chain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long duration = System.currentTimeMillis() - startTime;

            // 记录请求信息
            logRequest(requestWrapper);
            logResponse(responseWrapper, duration);

            // 将响应写回客户端，只要使用了 ContentCachingResponseWrapper 包装过后的，必须要手动将响应传给客户端
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        String clientIp = getClientIp(request);

        // 请求头
        Map<String, String> headers = getHeaders(request);

        // 请求体
        byte[] content = request.getContentAsByteArray();
        String body = content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";

        log.info("""
            \n=================== Request ===================
            Method    : {}
            URI       : {}
            Query     : {}
            Client IP : {}
            Headers   : {}
            Body      : {}
            ================================================
            """, method, uri, queryString, clientIp, headers, body);
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        int status = response.getStatus();
        byte[] content = response.getContentAsByteArray();
        String body = content.length > 0 ? new String(content, StandardCharsets.UTF_8) : "";

        log.info("""
            \n=================== Response ==================
            Status    : {}
            Duration  : {} ms
            Body      : {}
            =================================================
            """, status, duration, body);
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    private Map<String, String> getHeaders(HttpServletRequest request) {
        Map<String, String> headers = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            String value = request.getHeader(name);
            // 隐藏敏感信息
            if ("authorization".equalsIgnoreCase(name)) {
                value = "***HIDDEN***";
            }
            headers.put(name, value);
        }
        return headers;
    }
}