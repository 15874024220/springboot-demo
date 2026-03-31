package com.example.demo.websocket;

import com.example.demo.service.MessageService;
import jakarta.websocket.*;
import jakarta.websocket.server.PathParam;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@Component
@ServerEndpoint("/websocket/{userId}")
public class WebSocketServer {

    // 与某个客户端的连接会话
    private Session session;
    private String userId;

    // 线程安全的WebSocket集合
    private static final CopyOnWriteArraySet<WebSocketServer> webSockets = new CopyOnWriteArraySet<>();

    // 存储在线连接的用户ID和Session映射
    private static final ConcurrentHashMap<String, Session> sessionPool = new ConcurrentHashMap<>();

    // 注入Service（注意：由于WebSocket不是Spring管理的单例，需要使用静态方式）
    private static MessageService messageService;

    @Autowired
    public void setMessageService(MessageService messageService) {
        WebSocketServer.messageService = messageService;
    }

    /**
     * 连接成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            webSockets.add(this);
            sessionPool.put(userId, session);

            log.info("websocket消息: 用户[{}]连接成功，当前在线总数: {}", userId, webSockets.size());

            // 发送欢迎消息
            sendOneMessage(userId, "连接成功！欢迎用户：" + userId);

            // 广播在线人数
            broadcastOnlineCount();

        } catch (Exception e) {
            log.error("WebSocket连接失败", e);
        }
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, Session session, @PathParam(value = "userId") String userId) {
        log.info("websocket消息: 用户[{}]发送消息: {}", userId, message);

        try {
            // 保存消息到数据库
            if (messageService != null) {
                messageService.saveMessage(userId, message);
            }

            // 可以根据消息类型进行处理
            // 例如：私聊、群聊、心跳检测等

        } catch (Exception e) {
            log.error("处理消息失败", e);
            sendOneMessage(userId, "消息发送失败");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam(value = "userId") String userId) {
        try {
            webSockets.remove(this);
            sessionPool.remove(userId);
            log.info("websocket消息: 用户[{}]断开连接，当前在线总数: {}", userId, webSockets.size());

            // 广播在线人数
            broadcastOnlineCount();

        } catch (Exception e) {
            log.error("关闭连接失败", e);
        }
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error, @PathParam(value = "userId") String userId) {
        log.error("WebSocket用户[{}]发生错误", userId, error);
    }

    /**
     * 单点消息（发送给指定用户）
     */
    public void sendOneMessage(String userId, String message) {
        Session session = sessionPool.get(userId);
        if (session != null && session.isOpen()) {
            try {
                log.info("发送单点消息给用户[{}]: {}", userId, message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("发送单点消息失败", e);
            }
        } else {
            log.warn("用户[{}]不在线，消息发送失败", userId);
        }
    }

    /**
     * 广播消息（发送给所有在线用户）
     */
    public void broadcastMessage(String message) {
        log.info("广播消息: {}", message);
        for (WebSocketServer webSocket : webSockets) {
            try {
                webSocket.session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                log.error("广播消息失败", e);
            }
        }
    }

    /**
     * 广播在线人数
     */
    public void broadcastOnlineCount() {
        String message = "{\"type\":\"online_count\", \"count\":" + webSockets.size() + "}";
        broadcastMessage(message);
    }

    /**
     * 获取在线用户列表
     */
    public static ConcurrentHashMap<String, Session> getOnlineUsers() {
        return sessionPool;
    }

    /**
     * 判断用户是否在线
     */
    public static boolean isOnline(String userId) {
        return sessionPool.containsKey(userId);
    }

    /**
     * 获取在线人数
     */
    public static int getOnlineCount() {
        return webSockets.size();
    }
}