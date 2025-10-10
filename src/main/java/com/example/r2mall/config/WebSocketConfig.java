package com.example.r2mall.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.r2mall.handler.ChatWebSocketHandler;
import lombok.RequiredArgsConstructor;

/**
 * WebSocket配置类
 */
@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final ChatWebSocketHandler chatWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 注册WebSocket处理器
        // 路径格式: /im/{from_role}/{to_id}
        registry.addHandler(chatWebSocketHandler, "/im/**")
                .setAllowedOrigins("*"); // 允许跨域
    }
}

