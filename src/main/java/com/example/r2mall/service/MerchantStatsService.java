package com.example.r2mall.service;

import java.util.List;
import java.util.Map;

/**
 * 商家统计数据Service接口
 */
public interface MerchantStatsService {
    
    /**
     * 获取商家统计数据
     *
     * @param merchantId 商家ID
     * @return 包含totalProducts, totalOrders, totalSales, totalConsultations的Map
     */
    Map<String, Object> getMerchantStats(String merchantId);
    
    /**
     * 获取商品类型销售统计
     *
     * @param merchantId 商家ID
     * @return 各商品类型销售统计列表
     */
    List<Map<String, Object>> getCategorySalesStats(String merchantId);
}