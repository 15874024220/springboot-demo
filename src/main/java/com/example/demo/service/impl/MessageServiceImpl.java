package com.example.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.demo.entity.Message;
import com.example.demo.mapper.MessageMapper;
import com.example.demo.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    /**
     * 当前类继承（extends ServiceImpl<MessageMapper, Message>）了ServiceImpl，
     * ServiceImpl中已经存在了 MessageMapper 这个类，所以不需要再@Autowired进行注入了
     */
    @Autowired
    private MessageMapper messageMapper;

    @Override
    public void saveMessage(String userId, String message) {
        try {
            Message msg = new Message();
            msg.setUserId(userId);
            msg.setContent(message);
            msg.setType(1); // 1-私聊消息
            msg.setCreateTime(LocalDateTime.now());

            messageMapper.insert(msg);
            log.info("保存用户[{}]的消息成功", userId);
        } catch (Exception e) {
            log.error("保存消息失败", e);
        }
    }

    @Override
    public void saveBroadcastMessage(String message) {
        try {
            Message msg = new Message();
            msg.setContent(message);
            msg.setType(2); // 2-广播消息
            msg.setCreateTime(LocalDateTime.now());

            messageMapper.insert(msg);
            log.info("保存广播消息成功");
        } catch (Exception e) {
            log.error("保存广播消息失败", e);
        }
    }
}