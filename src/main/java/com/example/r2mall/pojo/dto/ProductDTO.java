package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

/**
 * 商品DTO
 */
@Data
@Schema(description = "商品DTO")
public class ProductDTO {

    @NotBlank(message = "商品名称不能为空")
    @Schema(description = "商品名称")
    private String name;

    @NotBlank(message = "商品图片不能为空")
    @Schema(description = "商品主图片URL")
    private String image;

    @NotNull(message = "商品价格不能为空")
    @Schema(description = "商品价格")
    private BigDecimal price;

    @NotNull(message = "库存数量不能为空")
    @Schema(description = "库存数量")
    private Integer stock;

    @Schema(description = "商品备注/描述")
    private String description;

    @Schema(description = "商品状态 (0: 下架, 1: 上架)")
    private Integer status;
}

