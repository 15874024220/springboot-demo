package com.example.demo.exception;

import com.example.demo.entity.ApiResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 参数校验异常（@Valid 校验失败）
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String errorMsg = errors.values().stream().collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMsg);

        return ApiResponse.validateFail(errorMsg, errors);
    }

    /**
     * 参数绑定异常（@RequestParam 绑定失败）
     */
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );

        String errorMsg = errors.values().stream().collect(Collectors.joining(", "));
        log.warn("参数绑定失败: {}", errorMsg);

        return ApiResponse.validateFail(errorMsg, errors);
    }

    /**
     * 单个参数校验异常（@RequestParam 上的 @Validated）
     */
    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, String> errors = ex.getConstraintViolations().stream()
                .collect(Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        ConstraintViolation::getMessage,
                        (v1, v2) -> v1 + "; " + v2
                ));

        String errorMsg = errors.values().stream().collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMsg);

        return ApiResponse.validateFail(errorMsg, errors);
    }

    /**
     * 参数类型不匹配（如 String 转 Integer 失败）
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String errorMsg = String.format("参数 '%s' 类型错误，期望类型: %s",
                ex.getName(), ex.getRequiredType().getSimpleName());
        log.warn("参数类型不匹配: {}", errorMsg);
        return ApiResponse.validateFail(errorMsg);
    }

    /**
     * 请求体格式错误（JSON 解析失败）
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        log.warn("请求体解析失败: {}", ex.getMessage());
        return ApiResponse.validateFail("请求体格式错误");
    }

    /**
     * 资源不存在（404）
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNoHandlerFound(NoHandlerFoundException ex) {
        log.warn("请求路径不存在: {} {}", ex.getHttpMethod(), ex.getRequestURL());
        return ApiResponse.notFound("请求的资源不存在");
    }

    /**
     * 请求方法不支持（如 POST 请求 GET 接口）
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ApiResponse<Void> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        log.warn("请求方法不支持: {}", ex.getMessage());
        return ApiResponse.fail(405, "请求方法不支持: " + ex.getMethod());
    }

    /**
     * Content-Type 不支持
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    public ApiResponse<Void> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex) {
        log.warn("不支持的Content-Type: {}", ex.getMessage());
        return ApiResponse.fail(415, "不支持的Content-Type类型");
    }

    /**
     * 文件上传大小超限
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleMaxUploadSizeExceeded(MaxUploadSizeExceededException ex) {
        Map<String, String> errorDetail = new HashMap<>();
        errorDetail.put("maxSize", String.valueOf(ex.getMaxUploadSize()));
        errorDetail.put("actualSize", String.valueOf(ex.getBody().toString().length()));

        log.warn("文件上传大小超限: max={}, actual={}", ex.getMaxUploadSize(), ex.getBody().toString().length());
        return ApiResponse.fail(400, "文件大小超出限制", errorDetail);
    }

    /**
     * 业务异常（IllegalArgumentException, IllegalStateException 等）
     */
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Void> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.warn("业务异常: {}", ex.getMessage());
        return ApiResponse.fail(400, ex.getMessage());
    }

    /**
     * 自定义业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ApiResponse<Void> handleBusinessException(BusinessException ex) {
        log.warn("业务异常: code={}, message={}", ex.getErrorCode(), ex.getMessage());

        // 根据业务错误码决定 HTTP 状态码
        Integer status = ex.getHttpStatus() != null ? ex.getHttpStatus() : 400;
        return ApiResponse.fail(status, ex.getMessage(), ex.getErrorCode(), ex.getMessage());
    }

    /**
     * 系统异常（兜底）
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleException(Exception ex, WebRequest request) {
        log.error("系统异常: ", ex);

        // 生产环境不返回具体错误信息
        if (isProduction()) {
            return ApiResponse.fail("系统繁忙，请稍后重试");
        }

        // 开发环境返回详细错误
        return ApiResponse.fail(500, ex.getMessage());
    }

    private boolean isProduction() {
        // 根据环境判断，从配置中心获取
        return false;
    }
}