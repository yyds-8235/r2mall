package com.example.r2mall.pojo.vo;

import com.example.r2mall.pojo.entity.OrderInfo;
import com.example.r2mall.pojo.entity.OrderItem;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.util.List;

/**
 * 订单详情VO
 */
@Data
@Schema(description = "订单详情VO")
public class OrderDetailVO {

    @Schema(description = "订单主信息")
    private OrderInfo orderInfo;

    @Schema(description = "订单商品列表")
    private List<OrderItem> items;
}

