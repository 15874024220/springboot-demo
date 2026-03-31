package com.example.demo.controller;

import com.example.demo.service.WebSocketService;
import com.example.demo.vo.MessageVO;
import com.example.demo.vo.Result;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/websocket")
public class WebSocketController {

    @Autowired
    private WebSocketService webSocketService;

    /**
     * 发送单点消息
     */
    @PostMapping("/send/one")
    public Result<Void> sendOneMessage(@Valid @RequestBody MessageVO messageVO) {
        log.info("发送单点消息请求: {}", messageVO);
        boolean success = webSocketService.sendMessageToUser(
                messageVO.getUserId(),
                messageVO.getMessage()
        );

        if (success) {
            return Result.success();
        } else {
            return Result.error("用户不在线或发送失败");
        }
    }

    /**
     * 发送广播消息
     */
    @PostMapping("/send/broadcast")
    public Result<Void> broadcastMessage(@RequestBody Map<String, String> params) {
        String message = params.get("message");
        log.info("发送广播消息请求: {}", message);
        webSocketService.broadcastMessage(message);
        return Result.success();
    }

    /**
     * 获取在线用户列表
     */
    @GetMapping("/online/users")
    public Result<Map<String, Object>> getOnlineUsers() {
        Map<String, Object> onlineInfo = webSocketService.getOnlineUsersInfo();
        return Result.success(onlineInfo);
    }

    /**
     * 获取在线人数
     */
    @GetMapping("/online/count")
    public Result<Integer> getOnlineCount() {
        int count = webSocketService.getOnlineCount();
        return Result.success(count);
    }

    /**
     * 踢出用户
     */
    @PostMapping("/kick/{userId}")
    public Result<Void> kickUser(@PathVariable String userId) {
        log.info("踢出用户请求: {}", userId);
        boolean success = webSocketService.kickUser(userId);

        if (success) {
            return Result.success();
        } else {
            return Result.error("用户不存在或踢出失败");
        }
    }
}