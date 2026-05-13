package com.example.demo.entity;

import lombok.Data;

import java.time.LocalDateTime;

@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    // HTTP状态码
    private Integer status;

    // 提示信息
    private String msg;

    // 响应数据
    private T data;

    // 业务状态码（0成功，非0失败）
    private Integer bizCode;

    // 业务提示信息
    private String bizMsg;

    // 请求路径
    private String path;

    // 响应时间
    private LocalDateTime time;

    // 业务状态码常量
    public interface BizCode {
        int SUCCESS = 0;           // 成功
        int FAIL = 1;              // 通用失败
        int VALIDATE_FAILED = 422; // 参数校验失败
        int UNAUTHORIZED = 401;    // 未认证
        int FORBIDDEN = 403;       // 无权限
        int NOT_FOUND = 404;       // 资源不存在
        int DUPLICATE = 409;       // 资源冲突
        int SERVER_ERROR = 500;    // 服务器错误

        int INVALID_ID = 100001;    // 无效id
        int USER_NOT_FOUND = 100002;    // 用户未发现
        int ACCESS_DENIED = 100003; // 无权限访问
        int INVALID_EMAIL = 100004; // 邮箱格式不正确
        int UNKNOWN_ERROR = 100005; // 未知错误
    }

    // ========== 成功响应 ==========

    /**
     * 成功响应（无数据）
     */
    public static <T> ApiResponse<T> success() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(200);
        response.setMsg("success");
        response.setBizCode(BizCode.SUCCESS);
        response.setBizMsg("操作成功");
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 成功响应（有数据）
     */
    public static <T> ApiResponse<T> success(T data) {
        ApiResponse<T> response = success();
        response.setData(data);
        return response;
    }

    /**
     * 成功响应（自定义业务提示）
     */
    public static <T> ApiResponse<T> success(String bizMsg, T data) {
        ApiResponse<T> response = success(data);
        response.setBizMsg(bizMsg);
        return response;
    }

    /**
     * 成功响应（全自定义）
     */
    public static <T> ApiResponse<T> success(String msg, String bizMsg, T data) {
        ApiResponse<T> response = success(data);
        response.setMsg(msg);
        response.setBizMsg(bizMsg);
        return response;
    }

    // ========== 失败响应 ==========

    /**
     * 失败响应（默认500错误）
     */
    public static <T> ApiResponse<T> fail() {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(500);
        response.setMsg("system error");
        response.setBizCode(BizCode.SERVER_ERROR);
        response.setBizMsg("系统繁忙，请稍后重试");
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 失败响应（自定义提示信息）
     */
    public static <T> ApiResponse<T> fail(String bizMsg) {
        ApiResponse<T> response = fail();
        response.setBizMsg(bizMsg);
        response.setMsg(bizMsg);
        return response;
    }

    /**
     * 失败响应（自定义HTTP状态码和业务提示）
     */
    public static <T> ApiResponse<T> fail(Integer status, String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMsg(bizMsg);
        response.setBizCode(status >= 500 ? BizCode.SERVER_ERROR : BizCode.FAIL);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 失败响应（完整自定义）
     */
    public static <T> ApiResponse<T> fail(Integer status, String msg, Integer bizCode, String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(status);
        response.setMsg(msg);
        response.setBizCode(bizCode);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 失败响应（带错误数据）
     */
    public static <T> ApiResponse<T> fail(Integer status, String bizMsg, T errorData) {
        ApiResponse<T> response = fail(status, bizMsg);
        response.setData(errorData);
        return response;
    }

    /**
     * 参数校验失败
     */
    public static <T> ApiResponse<T> validateFail(String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(400);
        response.setMsg("validation error");
        response.setBizCode(BizCode.VALIDATE_FAILED);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 参数校验失败（带详细错误）
     */
    public static <T> ApiResponse<T> validateFail(String bizMsg, T errorDetails) {
        ApiResponse<T> response = validateFail(bizMsg);
        response.setData(errorDetails);
        return response;
    }

    /**
     * 未授权
     */
    public static <T> ApiResponse<T> unauthorized(String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(401);
        response.setMsg("unauthorized");
        response.setBizCode(BizCode.UNAUTHORIZED);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 无权限
     */
    public static <T> ApiResponse<T> forbidden(String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(403);
        response.setMsg("forbidden");
        response.setBizCode(BizCode.FORBIDDEN);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 资源不存在
     */
    public static <T> ApiResponse<T> notFound(String bizMsg) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(404);
        response.setMsg("not found");
        response.setBizCode(BizCode.NOT_FOUND);
        response.setBizMsg(bizMsg);
        response.setTime(LocalDateTime.now());
        return response;
    }

    /**
     * 设置请求路径（在拦截器中自动设置）
     */
    public ApiResponse<T> withPath(String path) {
        this.setPath(path);
        return this;
    }
}