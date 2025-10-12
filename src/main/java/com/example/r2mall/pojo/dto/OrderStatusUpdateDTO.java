package com.example.r2mall.pojo.dto;

import lombok.Data;

/**
 * 订单商品项DTO
 */
@Data
public class OrderStatusUpdateDTO {

    /**
     * 订单状态 (0: 待支付, 1: 待发货, 2: 待收货, 3: 已完成)
     */
    private Integer status;

}