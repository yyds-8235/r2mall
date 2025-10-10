package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 订单商品项DTO
 */
@Data
@Schema(description = "订单商品项DTO")
public class OrderItemDTO {

    @NotNull(message = "商品ID不能为空")
    @Schema(description = "商品ID")
    private Long productId;

    @NotNull(message = "购买数量不能为空")
    @Schema(description = "购买数量")
    private Integer quantity;
}

