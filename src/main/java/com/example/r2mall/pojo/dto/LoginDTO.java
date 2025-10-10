package com.example.r2mall.pojo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 统一登录DTO
 */
@Data
@Schema(description = "统一登录DTO")
public class LoginDTO {

    @NotBlank(message = "登录ID不能为空")
    @Schema(description = "登录ID（用户名或商家号）")
    private String loginId;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码")
    private String password;

    @NotBlank(message = "登录类型不能为空")
    @Schema(description = "登录类型（user/merchant）")
    private String role;
}

