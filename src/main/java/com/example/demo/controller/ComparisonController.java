package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
     * ==================== redirect（重定向）====================
     *
     * 请求次数：2次（服务器响应302，客户端再次请求）
     * 地址栏：变化（变为目标URL）
     * 数据共享：不能共享request数据（两次请求）
     * 外部URL：支持（可以重定向到 https://www.baidu.com）
     * 性能：差（多一次请求）
     * 适用场景：表单提交后、登录后跳转、避免重复提交
     * 实现：response.sendRedirect(url)
     *
     * Controller 返回：
     * return "redirect:/users/" + userId;
     *
     * 浏览器行为：
     * 1. 访问 /create
     * 2. 收到 302 + Location: /users/123
     * 3. 地址栏变为 /users/123
     * 4. 发起新的 GET 请求 /users/123
     *
     *
     * ==================== forward（转发）====================
     *
     * 请求次数：1次（服务器内部转发）
     * 地址栏：不变（任然显示原始URL）
     * 数据共享：可以共享request数据
     * 外部URL：不支持（只能转发到同一应用）
     * 性能：好（服务器内部操作）
     * 适用场景：内部跳转、错误处理
     * 实现：request.getRequestDispatcher(url).forward(request, response)
     *
     * Controller 返回：
     * return "forward:/error";
     *
     * 浏览器行为：
     * 1. 访问 /forward
     * 2. 服务器内部转发到 /error
     * 3. 地址栏任然显示 /forward
     * 4. 响应内容是 /error 的结果
     *
     *
     * ==================== include（包含）====================
     *
     * 请求次数：1次（服务器内部操作）
     * 地址栏：不变
     * 数据共享：可以共享request数据
     * 外部URL：不支持（只能包含当前应用）
     * 性能：好
     * 适用场景：包含公共头部、尾部、侧边栏
     * 实现：request.getRequestDispatcher(url).include(request, response)
     *
     * Controller 返回：
     * return "include:/header";
     *
     * 浏览器行为：
     * 1. 访问 /page
     * 2. 服务器包含 /header 的内容
     * 3. 地址栏不变
     * 4. 响应中包含 /header 的输出
     */

// 实际代码对比
@Controller
@RequestMapping("/comparison")
public class ComparisonController {

    // redirect 示例
    @GetMapping("/create")
    public String createUser(@RequestParam String userId) {
//        User user = userService.create(form);
        // 重定向：防止刷新时重复提交
        return "redirect:/hello/" + userId;
        // 浏览器：POST → 302 → GET /users/123
    }

    // forward 示例
    @GetMapping("/forward")
    public String forwardExample() {
        // 转发：内部跳转到错误页面
        return "forward:/hello/error";
        // 浏览器地址栏：/forward 不变
    }

    // include 示例，只能用于模板，例如：Thymeleaf模板
    @GetMapping("/page")
    public String pageWithHeader() {
        // 包含：在页面中包含头部
        return "include:/common/header";
        // 浏览器地址栏：/page 不变
    }
}