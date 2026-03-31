package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    private Long id;
    private String userId;
    private String content;
    private Integer type; // 1-私聊 2-广播
    private LocalDateTime createTime;
}