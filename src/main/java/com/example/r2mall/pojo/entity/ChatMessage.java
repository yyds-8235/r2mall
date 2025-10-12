package com.example.r2mall.pojo.entity;

import lombok.Data;

/**
 * 聊天消息实体
 */
@Data
public class ChatMessage {

    /**
     * 消息ID
     */
    private Long id;
    
    /**
     * 发送者ID
     */
    private String fromUserId;
    
    /**
     * 接收者ID
     */
    private String toUserId;
    
    /**
     * 消息类型
     */
    private String messageType;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 时间戳
     */
    private Long timestamp;
}

