package com.example.demo.service;

import java.util.Map;

public interface WebSocketService {

    /**
     * 发送消息给指定用户
     */
    boolean sendMessageToUser(String userId, String message);

    /**
     * 广播消息给所有用户
     */
    void broadcastMessage(String message);

    /**
     * 获取在线用户信息
     */
    Map<String, Object> getOnlineUsersInfo();

    /**
     * 获取在线人数
     */
    int getOnlineCount();

    /**
     * 踢出用户
     */
    boolean kickUser(String userId);
}