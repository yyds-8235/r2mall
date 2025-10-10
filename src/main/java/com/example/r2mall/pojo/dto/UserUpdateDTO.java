package com.example.r2mall.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import java.time.LocalDate;

/**
 * 用户信息更新DTO
 */
@Data
@Schema(description = "用户信息更新DTO")
public class UserUpdateDTO {

    @Schema(description = "用户头像URL")
    private String avatar;

    @Schema(description = "性别 (0: 未知, 1: 男, 2: 女)")
    private Integer gender;

    @Schema(description = "出生年月")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
}

