package com.example.demo.service;

public interface MessageService {

    /**
     * 保存用户消息
     */
    void saveMessage(String userId, String message);

    /**
     * 保存广播消息
     */
    void saveBroadcastMessage(String message);
}