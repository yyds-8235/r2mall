package com.example.r2mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.r2mall.pojo.entity.ChatMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 聊天消息Mapper接口
 */
@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {

    /**
     * 获取聊天历史记录
     *
     * @param fromUserId 发送者ID
     * @param toUserId   接收者ID
     * @param offset     页码
     * @param size       每页数量
     * @return 聊天消息列表
     */
    @Select("SELECT * FROM chat_message WHERE (from_user_id = #{fromUserId} AND to_user_id = #{toUserId}) OR (from_user_id = #{toUserId} AND to_user_id = #{fromUserId}) ORDER BY timestamp ASC LIMIT #{offset}, #{size}")
    List<ChatMessage> getChatHistory(@Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId, @Param("offset") int offset, @Param("size") int size);

    /**
     * 获取聊天记录总数
     *
     * @param fromUserId 发送者ID
     * @param toUserId   接收者ID
     * @return 消息总数
     */
    @Select("SELECT COUNT(*) FROM chat_message WHERE (from_user_id = #{fromUserId} AND to_user_id = #{toUserId}) OR (from_user_id = #{toUserId} AND to_user_id = #{fromUserId})")
    int countChatHistory(@Param("fromUserId") String fromUserId, @Param("toUserId") String toUserId);
    
    /**
     * 获取商家的聊天会话列表
     *
     * @param merchantId 商家ID
     * @return 聊天会话列表
     */
    @Select("WITH LastMessage AS ( " +
            "SELECT " +
            "CASE " +
            "WHEN from_user_id = #{merchantId} THEN to_user_id " +
            "ELSE from_user_id " +
            "END AS chatPartnerId, " +
            "MAX(timestamp) AS maxTime " +
            "FROM " +
            "chat_message " +
            "WHERE " +
            "from_user_id = #{merchantId} OR to_user_id = #{merchantId} " +
            "GROUP BY " +
            "chatPartnerId " +
            ") " +
            " " +
            "SELECT " +
            "u.id AS userId, " +
            "u.username AS userName, " +
            "u.avatar AS avatar, " +
            "cm.content AS lastMessage, " +
            "cm.timestamp AS lastMessageTime, " +
            "( " +
            "SELECT " +
            "COUNT(t.id) " +
            "FROM " +
            "chat_message t " +
            "WHERE " +
            "(t.from_user_id = #{merchantId} AND t.to_user_id = u.id) " +
            "OR (t.to_user_id = #{merchantId} AND t.from_user_id = u.id) " +
            ") AS totalMessages " +
            "FROM " +
            "user u " +
            "INNER JOIN " +
            "LastMessage lm ON u.id = lm.chatPartnerId " +
            "INNER JOIN " +
            "chat_message cm ON " +
            "(cm.from_user_id = u.id OR cm.to_user_id = u.id) " +
            "AND cm.timestamp = lm.maxTime " +
            "AND (cm.from_user_id = #{merchantId} OR cm.to_user_id = #{merchantId}) " +
            " " +
            "ORDER BY " +
            "lastMessageTime DESC")
    List<Map<String, Object>> getMerchantChatSessions(@Param("merchantId") String merchantId);
}