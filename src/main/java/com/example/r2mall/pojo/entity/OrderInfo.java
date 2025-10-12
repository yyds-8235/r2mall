package com.example.r2mall.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类
 */
@Data
@TableName("order_info")
public class OrderInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 订单编号 (业务唯一)
     */
    @TableField("order_no")
    private String orderNo;

    /**
     * 下单用户ID
     */
    @TableField("user_id")
    private Long userId;

    /**
     * 实付款金额
     */
    @TableField("total_amount")
    private BigDecimal totalAmount;

    /**
     * 收货地址（快照）
     */
    @TableField("shipping_address")
    private String shippingAddress;

    /**
     * 订单状态 (0: 待支付, 1: 已支付/待发货, 2: 已发货/配送中)
     */
    @TableField("status")
    private Integer status;

    /**
     * 预计送达时间
     */
    @TableField("delivery_time")
    private LocalDateTime deliveryTime;

    /**
     * 下单时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 支付时间
     */
    @TableField("payment_time")
    private LocalDateTime paymentTime;
}

