package com.example.r2mall.handler;

import com.example.r2mall.pojo.entity.ChatMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket聊天处理器
 */
@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();
    
    // 存储所有在线用户的会话，key格式: "user_101" 或 "merchant_20"
    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 从URI中解析角色和ID
        // URI格式: /im/{from_role}/{to_id}
        // 例如: /im/user/20 表示用户连接到商家20
        // 例如: /im/merchant/101 表示商家连接到用户101
        
        String path = session.getUri().getPath();
        String[] parts = path.split("/");
        
        if (parts.length >= 4) {
            String fromRole = parts[2]; // user 或 merchant
            String toId = parts[3];     // 目标ID
            
            // 构建会话key
            String sessionKey = fromRole + "_" + session.getId();
            sessions.put(sessionKey, session);
            
            // 存储到session属性中，方便后续使用
            session.getAttributes().put("fromRole", fromRole);
            session.getAttributes().put("toId", toId);
            session.getAttributes().put("sessionKey", sessionKey);
            
            log.info("WebSocket连接建立: {} 连接到 {}", fromRole, toId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            // 解析消息
            String payload = message.getPayload();
            ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
            
            // 获取发送者和接收者信息
            String fromUserId = chatMessage.getFromUserId();
            String toUserId = chatMessage.getToUserId();
            
            log.info("收到消息: 从 {} 发送给 {}, 内容: {}", fromUserId, toUserId, chatMessage.getContent());
            
            // 找到接收者的会话并转发消息
            // 需要查找所有会话，因为接收者可能是user也可能是merchant
            boolean sent = false;
            for (Map.Entry<String, WebSocketSession> entry : sessions.entrySet()) {
                WebSocketSession targetSession = entry.getValue();
                if (targetSession.isOpen()) {
                    // 检查这个会话是否是目标接收者的会话
                    String targetRole = (String) targetSession.getAttributes().get("fromRole");
                    String targetToId = (String) targetSession.getAttributes().get("toId");
                    
                    // 如果是接收者的会话，转发消息
                    if (isTargetSession(targetRole, targetToId, fromUserId, toUserId)) {
                        targetSession.sendMessage(new TextMessage(payload));
                        sent = true;
                        log.info("消息已转发给接收者");
                        break;
                    }
                }
            }
            
            if (!sent) {
                log.warn("接收者不在线或未找到对应会话");
            }
            
        } catch (Exception e) {
            log.error("处理WebSocket消息异常", e);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String sessionKey = (String) session.getAttributes().get("sessionKey");
        if (sessionKey != null) {
            sessions.remove(sessionKey);
            log.info("WebSocket连接关闭: {}", sessionKey);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("WebSocket传输错误", exception);
        session.close();
    }

    /**
     * 判断是否是目标会话
     * @param targetRole 目标会话的角色
     * @param targetToId 目标会话连接到的对方ID
     * @param fromUserId 消息发送者ID
     * @param toUserId 消息接收者ID
     */
    private boolean isTargetSession(String targetRole, String targetToId, String fromUserId, String toUserId) {
        // 逻辑：如果目标会话的toId等于fromUserId，说明这个会话就是要接收消息的会话
        return targetToId.equals(fromUserId);
    }
}

