package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 商家信息更新DTO
 */
@Data
@Schema(description = "商家信息更新DTO")
public class MerchantUpdateDTO {

    @Schema(description = "店铺名称")
    private String shopName;

    @Schema(description = "商家/店铺头像URL")
    private String avatar;
}

