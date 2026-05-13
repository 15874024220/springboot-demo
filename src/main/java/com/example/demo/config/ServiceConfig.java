package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Map;
import java.util.function.Supplier;

/**
 * 根据状态动态返回不同的 Bean 实现。
 *
 * 重要提醒：
 *      动态参数的限制：@Bean 方法的参数必须是 Spring 能注入的（如 @Value、@Autowired 等），不能是普通的运行时参数。
 *      如果你的 status 是运行时动态决定的（比如来自用户请求），那就不能用 @Bean，而应该用工厂模式：
 * @author jork
 */
@Configuration
public class ServiceConfig {

    /**
     * 方式一：使用普通 switch（最直接）
     */
    @Bean
    @Primary
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public String getService1(@Value("${service.status}") int status) {
        switch (status) {
            case 1:
                return "1";
            case 2:
                return "2";
            case 3:
                return "3";
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }

    /**
     *
     * 方式二：使用 Map 工厂模式（更优雅）
     *
     */
    @Bean
    public String getService2(@Value("${service.status}") int status) {
        return serviceMap().getOrDefault(status,
                        () -> { throw new IllegalArgumentException("Unknown status"); })
                .get();
    }

    @Bean
    public Map<Integer, Supplier<String>> serviceMap() {
        return Map.of(
                1, () -> "1",
                2, () -> "2",
                3, () -> "3"
        );
    }

    /**
     * 方式三：条件注解（Spring 推荐）
     */
    @Bean
    @ConditionalOnProperty(name = "service.status", havingValue = "1")
    public String serviceImpl1() {
        return "1";
    }

    @Bean
    @ConditionalOnProperty(name = "service.status", havingValue = "2")
    public String serviceImpl2() {
        return "2";
    }

    @Bean
    @ConditionalOnProperty(name = "service.status", havingValue = "3")
    public String serviceImpl3() {
        return "3";
    }
}
