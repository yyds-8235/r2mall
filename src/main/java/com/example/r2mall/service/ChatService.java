package com.example.r2mall.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.r2mall.pojo.entity.ChatMessage;

import java.util.List;
import java.util.Map;

/**
 * 聊天服务接口
 */
public interface ChatService extends IService<ChatMessage> {
    
    /**
     * 获取聊天历史记录
     * @param fromUserId 发送者ID
     * @param toUserId 接收者ID
     * @param page 页码
     * @param size 每页数量
     * @return 包含聊天记录和分页信息的Map
     */
    Map<String, Object> getChatHistory(String fromUserId, String toUserId, int page, int size);
    
    /**
     * 发送聊天消息
     * @param chatMessage 聊天消息实体
     * @return 保存后的聊天消息
     */
    ChatMessage sendChatMessage(ChatMessage chatMessage);
    
    /**
     * 获取商家的聊天会话列表
     * @param merchantId 商家ID
     * @return 聊天会话列表
     */
    List<Map<String, Object>> getMerchantChatSessions(String merchantId);
}