package com.example.r2mall.service.impl;

import com.example.r2mall.mapper.MerchantStatsMapper;
import com.example.r2mall.service.MerchantStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商家统计数据Service实现类
 */
@Service
@RequiredArgsConstructor
public class MerchantStatsServiceImpl implements MerchantStatsService {
    
    private final MerchantStatsMapper merchantStatsMapper;
    
    @Override
    public Map<String, Object> getMerchantStats(String merchantId) {
        Map<String, Object> stats = new HashMap<>();
        
        // 获取商品总数
        int totalProducts = merchantStatsMapper.getTotalProducts(merchantId);
        
        // 获取订单总数
        int totalOrders = merchantStatsMapper.getTotalOrders(merchantId);
        
        // 获取总销售额
        Double totalSales = merchantStatsMapper.getTotalSales(merchantId);
        if (totalSales == null) {
            totalSales = 0.0;
        }
        
        // 获取咨询人次
        int totalConsultations = merchantStatsMapper.getTotalConsultations(merchantId);
        
        // 设置返回结果
        stats.put("totalProducts", totalProducts);
        stats.put("totalOrders", totalOrders);
        stats.put("totalSales", totalSales);
        stats.put("totalConsultations", totalConsultations);
        
        return stats;
    }
    
    @Override
    public List<Map<String, Object>> getCategorySalesStats(String merchantId) {
        return merchantStatsMapper.getCategorySalesStats(merchantId);
    }
}