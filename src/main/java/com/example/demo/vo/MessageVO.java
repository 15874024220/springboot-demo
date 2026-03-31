package com.example.demo.vo;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageVO {

    @NotBlank(message = "用户ID不能为空")
    private String userId;

    @NotBlank(message = "消息内容不能为空")
    private String message;
}