package com.example.r2mall.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 商家统计数据Mapper接口
 */
@Mapper
public interface MerchantStatsMapper {
    
    /**
     * 获取商家商品总数
     *
     * @param merchantId 商家ID
     * @return 商品总数
     */
    @Select("SELECT COUNT(*) FROM product WHERE merchant_id = #{merchantId} AND status = '1'")
    int getTotalProducts(@Param("merchantId") String merchantId);
    
    /**
     * 获取商家订单总数
     *
     * @param merchantId 商家ID
     * @return 订单总数
     */
    @Select("SELECT COUNT(DISTINCT oi.order_no) FROM order_item oi INNER JOIN product p ON oi.product_id = p.id WHERE p.merchant_id = #{merchantId} ")
    int getTotalOrders(@Param("merchantId") String merchantId);
    
    /**
     * 获取商家总销售额
     *
     * @param merchantId 商家ID
     * @return 总销售额
     */
    @Select("SELECT SUM(oi.quantity * oi.price) FROM order_item oi INNER JOIN product p ON oi.product_id = p.id WHERE p.merchant_id = #{merchantId} ")
    Double getTotalSales(@Param("merchantId") String merchantId);
    
    /**
     * 获取商家咨询人次
     *
     * @param merchantId 商家ID
     * @return 咨询人次
     */
    @Select("SELECT COUNT(DISTINCT CASE WHEN from_user_id = #{merchantId} THEN to_user_id ELSE from_user_id END) FROM chat_message WHERE from_user_id = #{merchantId} OR to_user_id = #{merchantId}")
    int getTotalConsultations(@Param("merchantId") String merchantId);
    
    /**
     * 获取各商品类型的销售数量和销售额
     *
     * @param merchantId 商家ID
     * @return 商品类型销售统计列表
     */
    @Select("SELECT p.category, COUNT(oi.id) AS count, SUM(oi.quantity * oi.price) AS sales  FROM order_item oi INNER JOIN product p ON oi.product_id = p.id WHERE p.merchant_id = #{merchantId} GROUP BY p.category ORDER BY sales DESC")
    List<Map<String, Object>> getCategorySalesStats(@Param("merchantId") String merchantId);
}