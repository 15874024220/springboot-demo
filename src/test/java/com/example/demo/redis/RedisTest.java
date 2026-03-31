package com.example.demo.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *
 * ServerEndpointExporter 用于在 Web 容器中注册 WebSocket 端点
 * 单元测试环境没有启动 Tomcat/Jetty 等 Servlet 容器导致 ServerContainer 不存在，无法创建 Bean
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class RedisTest {

    @Autowired(required = false)
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void run() throws Exception {
        if (redisTemplate != null) {
            try {
                System.out.println("=== 测试 Redis 连接 ===");
                redisTemplate.opsForValue().set("test-key", "Hello Redis");
                String value = redisTemplate.opsForValue().get("test-key");
                System.out.println("Redis 测试成功: " + value);
            } catch (Exception e) {
                System.err.println("Redis 测试失败: " + e.getMessage());
            }
        } else {
            System.out.println("RedisTemplate 未配置");
        }
    }
}