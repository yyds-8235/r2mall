package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

/**
 * 订单创建DTO
 */
@Data
@Schema(description = "订单创建DTO")
public class OrderCreateDTO {

    @NotNull(message = "收货地址ID不能为空")
    @Schema(description = "收货地址ID")
    private Long addressId;

    @NotEmpty(message = "订单商品不能为空")
    @Schema(description = "订单商品列表")
    private List<OrderItemDTO> items;
}

