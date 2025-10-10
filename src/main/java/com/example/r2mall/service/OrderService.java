package com.example.r2mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.pojo.dto.OrderCreateDTO;
import com.example.r2mall.pojo.entity.OrderInfo;
import com.example.r2mall.pojo.vo.OrderDetailVO;

/**
 * 订单服务接口
 */
public interface OrderService {

    /**
     * 创建订单
     */
    String createOrder(Long userId, OrderCreateDTO dto);

    /**
     * 模拟支付
     */
    void payOrder(Long userId, String orderNo);

    /**
     * 获取用户订单列表
     */
    Page<OrderInfo> getOrderList(Long userId, Integer page, Integer size);

    /**
     * 获取订单详情
     */
    OrderDetailVO getOrderDetail(Long userId, String orderNo);
}

