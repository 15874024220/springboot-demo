package com.example.demo.config;

import com.example.demo.filter.LoggingFilter;
import com.example.demo.filter.MyFilterWithAnnotation;
import com.example.demo.filter.RateLimitFilter;
import com.example.demo.filter.XssFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

// 最佳实践：使用 FilterRegistrationBean
@Configuration
public class FilterConfig {
    @Bean
    public FilterRegistrationBean<MyFilterWithAnnotation> myFilter() {
        FilterRegistrationBean<MyFilterWithAnnotation> bean = new FilterRegistrationBean<>();
        bean.setFilter(new MyFilterWithAnnotation());  // 不需要任何注解
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);  // 最高优先级
        bean.addUrlPatterns("/*");
        return bean;
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilter() {
        FilterRegistrationBean<LoggingFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new LoggingFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);  // 次高优先级
        return bean;
    }

    @Bean
    public FilterRegistrationBean<RateLimitFilter> rateLimitFilter() {
        FilterRegistrationBean<RateLimitFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new RateLimitFilter());
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE + 2);
        return bean;
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilter() {
        FilterRegistrationBean<XssFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new XssFilter());
        bean.setOrder(Ordered.LOWEST_PRECEDENCE);  // 最低优先级
        return bean;
    }
}