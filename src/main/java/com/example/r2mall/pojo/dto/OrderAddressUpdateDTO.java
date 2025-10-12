package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 订单地址更新DTO
 */
@Data
@Schema(description = "订单地址更新DTO")
public class OrderAddressUpdateDTO {

    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "新的收货地址（格式：收货人 电话 详细地址）")
    private String shippingAddress;
}

