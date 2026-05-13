package com.example.demo.filter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import javax.cache.integration.CacheLoaderException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *  案例2：请求限流过滤器
 */
@Slf4j
public class RateLimitFilter implements Filter {

    // 使用 Guava Cache 存储 IP 的请求计数
    private LoadingCache<String, AtomicInteger> requestCounts;

    // 限流配置
    private static final int MAX_REQUESTS_PER_MINUTE = 100;  // 每分钟最大请求数
    private static final int MAX_REQUESTS_PER_SECOND = 10;   // 每秒最大请求数

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化缓存：每分钟过期
        requestCounts = CacheBuilder.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES)
                .build(new CacheLoader<String, AtomicInteger>() {
                    @Override
                    public AtomicInteger load(String key) throws CacheLoaderException {
                        return new AtomicInteger(0);
                    }

                    @Override
                    public Map<String, AtomicInteger> loadAll(Iterable<? extends String> iterable) throws CacheLoaderException {
                        return Map.of();
                    }
                });
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String clientIp = getClientIp(req);

        // 检查限流
        if (isRateLimited(clientIp)) {
            log.warn("IP {} 请求频率超限", clientIp);
            res.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            res.setContentType("application/json;charset=UTF-8");
            res.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后再试\"}");
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isRateLimited(String clientIp) {
        AtomicInteger count = requestCounts.getUnchecked(clientIp);
        int currentCount = count.incrementAndGet();

        // 每秒限流检查（简化实现）
        if (currentCount > MAX_REQUESTS_PER_SECOND &&
                System.currentTimeMillis() % 60000 < 1000) {
            return true;
        }

        // 每分钟限流检查
        return currentCount > MAX_REQUESTS_PER_MINUTE;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}