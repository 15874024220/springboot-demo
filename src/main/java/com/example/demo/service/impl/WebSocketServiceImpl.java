package com.example.demo.service.impl;

import com.example.demo.service.MessageService;
import com.example.demo.service.WebSocketService;
import com.example.demo.websocket.WebSocketServer;
import jakarta.websocket.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebSocketServiceImpl implements WebSocketService {

    @Autowired
    private MessageService messageService;

    @Override
    public boolean sendMessageToUser(String userId, String message) {
        try {
            // 保存消息记录
            messageService.saveMessage(userId, message);

            // 创建WebSocket实例发送消息
            WebSocketServer webSocketServer = new WebSocketServer();
            webSocketServer.sendOneMessage(userId, message);

            return true;
        } catch (Exception e) {
            log.error("发送消息给用户[{}]失败", userId, e);
            return false;
        }
    }

    @Override
    public void broadcastMessage(String message) {
        try {
            // 保存广播消息
            messageService.saveBroadcastMessage(message);

            // 创建WebSocket实例广播消息
            WebSocketServer webSocketServer = new WebSocketServer();
            webSocketServer.broadcastMessage(message);

        } catch (Exception e) {
            log.error("广播消息失败", e);
        }
    }

    @Override
    public Map<String, Object> getOnlineUsersInfo() {
        Map<String, Object> result = new HashMap<>();
        ConcurrentHashMap<String, Session> onlineUsers = WebSocketServer.getOnlineUsers();

        result.put("count", onlineUsers.size());
        result.put("users", onlineUsers.keySet());
        result.put("onlineList", onlineUsers.keySet());

        return result;
    }

    @Override
    public int getOnlineCount() {
        return WebSocketServer.getOnlineCount();
    }

    @Override
    public boolean kickUser(String userId) {
        try {
            Session session = WebSocketServer.getOnlineUsers().get(userId);
            if (session != null && session.isOpen()) {
                session.close();
                return true;
            }
            return false;
        } catch (Exception e) {
            log.error("踢出用户[{}]失败", userId, e);
            return false;
        }
    }
}