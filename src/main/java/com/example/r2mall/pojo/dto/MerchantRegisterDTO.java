package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 商家注册DTO
 */
@Data
@Schema(description = "商家注册DTO")
public class MerchantRegisterDTO {

    @NotBlank(message = "商家号不能为空")
    @Schema(description = "商家号")
    private String merchantNo;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "店铺名称不能为空")
    @Schema(description = "店铺名称")
    private String shopName;
}

