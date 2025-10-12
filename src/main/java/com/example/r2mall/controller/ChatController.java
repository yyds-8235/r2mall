package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.entity.ChatMessage;
import com.example.r2mall.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 聊天控制器
 */
@RestController
@RequestMapping("/api/user/chat")
@RequiredArgsConstructor
@Tag(name = "聊天功能", description = "用户聊天相关接口")
public class ChatController {
    
    private final ChatService chatService;
    
    /**
     * 获取聊天历史记录
     * @param toUserId 接收者ID
     * @param page 页码
     * @param size 每页数量
     * @return 聊天历史记录列表
     */
    @GetMapping("/history/{toUserId}")
    @Operation(summary = "获取聊天历史记录")
    public Result<Map<String, Object>> getChatHistory(@PathVariable String toUserId, 
                                                    @RequestParam(defaultValue = "1") int page, 
                                                    @RequestParam(defaultValue = "50") int size) {
        try {
            // 获取当前登录用户ID
            Long userId = StpUtil.getLoginIdAsLong();
            String fromUserId = userId.toString();
            
            // 调用服务获取聊天历史
            Map<String, Object> history = chatService.getChatHistory(fromUserId, toUserId, page, size);
            
            return Result.success(history);
        } catch (Exception e) {
            return Result.error("获取聊天历史失败：" + e.getMessage());
        }
    }
    
    /**
     * 发送聊天消息
     * @param requestBody 请求体
     * @return 发送的消息
     */
    @PostMapping("/send")
    @Operation(summary = "发送聊天消息")
    public Result<ChatMessage> sendChatMessage(@RequestBody Map<String, Object> requestBody) {
        try {
            // 获取当前登录用户ID
            Long userId = StpUtil.getLoginIdAsLong();
            String fromUserId = userId.toString();
            
            // 构建聊天消息
            ChatMessage chatMessage = new ChatMessage();
            chatMessage.setFromUserId(fromUserId);
            chatMessage.setToUserId((String) requestBody.get("toUserId"));
            chatMessage.setMessageType((String) requestBody.get("messageType"));
            chatMessage.setContent((String) requestBody.get("content"));
            chatMessage.setTimestamp(System.currentTimeMillis());
            
            // 调用服务发送消息
            ChatMessage savedMessage = chatService.sendChatMessage(chatMessage);
            
            return Result.success(savedMessage);
        } catch (Exception e) {
            return Result.error("发送消息失败：" + e.getMessage());
        }
    }
}
