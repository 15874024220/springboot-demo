package com.example.demo.config.aspect;

import com.example.demo.filter.MyFilterWithAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LogAspect {
    private static final Logger logger = LoggerFactory.getLogger(MyFilterWithAnnotation.class);

    @Before("execution(* com.example.demo.service.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        logger.info("【AOP】方法执行前: {}", joinPoint.getSignature().getName());
    }
}