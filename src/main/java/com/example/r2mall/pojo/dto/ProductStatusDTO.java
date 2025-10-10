package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 商品状态更新DTO
 */
@Data
@Schema(description = "商品状态更新DTO")
public class ProductStatusDTO {

    @NotNull(message = "商品状态不能为空")
    @Schema(description = "商品状态 (0: 下架, 1: 上架)")
    private Integer status;
}

