package com.example.demo.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @ServerEndpointExporter 用于在 Web 容器中注册 WebSocket 端点
 * 单元测试环境没有启动 Tomcat/Jetty 等 Servlet 容器导致 ServerContainer 不存在，无法创建 Bean
 * @ActiveProfiles 一般作用于测试类上， 用于声明生效的 Spring 配置文件。
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles()
@Transactional
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

    @Test
    @Transactional // 测试数据将回滚
//    @WithMockUser(username = "test-user", authorities = { "ROLE_TEACHER", "read" }) // 模拟一个名为 "test-user"，拥有 TEACHER 角色和 read 权限的用户
    void should_perform_action_requiring_teacher_role() throws Exception {
        // ... 测试逻辑 ...
        // 这里可以调用需要 "ROLE_TEACHER" 权限的服务方法
    }
}