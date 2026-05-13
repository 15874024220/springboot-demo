package com.example.demo.controller;

import com.example.demo.annotation.IgnoreApiResponse;
import com.example.demo.dto.UserDTO;
import com.example.demo.entity.ApiResponse;
import com.example.demo.exception.BusinessException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
@Validated  // 开启方法级别参数校验
public class UserController {

    /**
     * 基础使用：直接返回对象，会被自动包装
     */
    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id) {
        if (id <= 0) {
            throw new BusinessException("用户ID必须大于0", ApiResponse.BizCode.INVALID_ID);
        }

        UserDTO user = new UserDTO();
        user.setId(id);
        user.setName("张三");
        user.setEmail("zhangsan@example.com");
        return user;  // 自动包装为 ApiResponse.success(user)
    }

    /**
     * 返回 List，自动包装
     */
    @GetMapping
    public List<UserDTO> getUsers() {
        return Arrays.asList(
                new UserDTO(1L, "张三", "zhangsan@example.com"),
                new UserDTO(2L, "李四", "lisi@example.com")
        );  // 自动包装为 ApiResponse.success(list)
    }

    /**
     * 参数校验（@Valid 自动触发异常，由全局处理器处理）
     */
    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody UserDTO userDTO) {
        // 业务逻辑...
        log.info("创建用户: {}", userDTO);

        // 手动返回 ApiResponse（不会被二次包装）
        return ApiResponse.success("用户创建成功", userDTO);
    }

    /**
     * 方法级别参数校验
     */
    @GetMapping("/validate")
    public ApiResponse<String> validateParam(
            @RequestParam @NotNull(message = "用户名不能为空") String username,
            @RequestParam @Min(value = 1, message = "年龄必须大于0") Integer age) {

        return ApiResponse.success("校验通过", "username=" + username + ", age=" + age);
    }

    /**
     * 手动返回失败响应
     */
    @GetMapping("/check/{username}")
    public ApiResponse<Void> checkUsername(@PathVariable String username) {
        if ("admin".equalsIgnoreCase(username)) {
            return ApiResponse.fail(409, "用户名已被占用", ApiResponse.BizCode.DUPLICATE, "用户名已存在");
        }
        return ApiResponse.success();
    }

    /**
     * 业务异常示例
     */
    @GetMapping("/error/{type}")
    public ApiResponse<UserDTO> testError(@PathVariable String type) {
        switch (type) {
            case "notfound":
                throw new BusinessException("用户不存在", ApiResponse.BizCode.USER_NOT_FOUND, 404);
            case "forbidden":
                throw new BusinessException("无权限访问", ApiResponse.BizCode.ACCESS_DENIED, 403);
            case "validate":
                throw new BusinessException("邮箱格式不正确", ApiResponse.BizCode.INVALID_EMAIL, 400);
            default:
                throw new BusinessException("未知错误", ApiResponse.BizCode.UNKNOWN_ERROR);
        }
    }

    /**
     * 忽略自动包装（返回原始字符串）
     */
    @GetMapping("/raw")
    @IgnoreApiResponse
    public String getRawData() {
        return "原始数据，不会被包装";
    }

    /**
     * 返回 void（自动包装成功空响应）
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        log.info("删除用户: {}", id);
        // 没有返回值，自动包装为 ApiResponse.success()
    }
}