package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 收货地址DTO
 */
@Data
@Schema(description = "收货地址DTO")
public class AddressDTO {

    @NotBlank(message = "收货人姓名不能为空")
    @Schema(description = "收货人姓名")
    private String recipientName;

    @NotBlank(message = "联系电话不能为空")
    @Schema(description = "联系电话")
    private String phone;

    @NotBlank(message = "收货地址不能为空")
    @Schema(description = "详细收货地址")
    private String address;

    @Schema(description = "是否为默认地址 (0: 否, 1: 是)")
    private Integer isDefault;
}

