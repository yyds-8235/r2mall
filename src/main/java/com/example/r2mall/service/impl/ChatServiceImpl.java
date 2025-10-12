package com.example.r2mall.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.r2mall.mapper.ChatMessageMapper;
import com.example.r2mall.pojo.entity.ChatMessage;
import com.example.r2mall.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 聊天服务实现类
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl extends ServiceImpl<ChatMessageMapper, ChatMessage> implements ChatService {
    
    private final ChatMessageMapper chatMessageMapper;
    
    @Override
    public Map<String, Object> getChatHistory(String fromUserId, String toUserId, int page, int size) {
        // 计算偏移量
        int offset = (page - 1) * size;
        
        // 获取聊天历史记录
        List<ChatMessage> records = chatMessageMapper.getChatHistory(fromUserId, toUserId, offset, size);
        
        // 获取总数
        int total = chatMessageMapper.countChatHistory(fromUserId, toUserId);
        
        // 构建返回结果
        Map<String, Object> result = new HashMap<>();
        result.put("records", records);
        result.put("total", total);
        result.put("current", page);
        result.put("size", size);
        
        return result;
    }
    
    @Override
    public ChatMessage sendChatMessage(ChatMessage chatMessage) {
        // 设置时间戳
        if (chatMessage.getTimestamp() == null) {
            chatMessage.setTimestamp(System.currentTimeMillis());
        }
        
        // 保存消息到数据库
        this.save(chatMessage);
        
        return chatMessage;
    }
    
    @Override
    public List<Map<String, Object>> getMerchantChatSessions(String merchantId) {
        // 从数据库获取商家的聊天会话列表
        List<Map<String, Object>> sessions = chatMessageMapper.getMerchantChatSessions(merchantId);
        
        // 为每个会话设置unreadCount为1（因为目前没有定义未读消息的逻辑）
//        for (Map<String, Object> session : sessions) {
//            session.put("unreadCount", 1);
//        }
        
        return sessions;
    }
}