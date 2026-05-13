package com.example.demo.config;

import com.example.demo.annotation.IgnoreApiResponse;
import com.example.demo.entity.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "com.example.demo.controller")
@Order(1)
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        // 如果返回类型已经是 ApiResponse，不需要包装
        if (returnType.getParameterType() == ApiResponse.class) {
            return false;
        }

        // 如果方法上有 @IgnoreApiResponse 注解，不包装
        if (returnType.hasMethodAnnotation(IgnoreApiResponse.class)) {
            return false;
        }

        // 如果是 String 类型，需要特殊处理
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        // 处理 String 类型返回值（避免类型转换异常）
        if (body instanceof String) {
            try {
                ApiResponse<String> apiResponse = ApiResponse.success((String) body);
                return objectMapper.writeValueAsString(apiResponse);
            } catch (Exception e) {
                log.error("序列化String响应失败", e);
                return ApiResponse.fail("响应序列化失败");
            }
        }

        // 已经是 ApiResponse，直接返回
        if (body instanceof ApiResponse) {
            return body;
        }

        // 包装成功响应
        if (body == null) {
            return ApiResponse.success();
        }

        return ApiResponse.success(body);
    }
}