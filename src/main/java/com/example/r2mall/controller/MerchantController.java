package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.MerchantUpdateDTO;
import com.example.r2mall.pojo.dto.PasswordUpdateDTO;
import com.example.r2mall.pojo.entity.Merchant;
import com.example.r2mall.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商家个人中心控制器
 */
@RestController
@RequestMapping("/api/merchant")
@RequiredArgsConstructor
@Tag(name = "商家个人中心", description = "商家个人信息管理接口")
public class MerchantController {

    private final MerchantService merchantService;

    @GetMapping("/profile")
    @Operation(summary = "获取商家信息")
    public Result<Merchant> getProfile() {
        Long merchantId = StpUtil.getLoginIdAsLong();
        Merchant merchant = merchantService.getById(merchantId);
        // 不返回密码
        merchant.setPassword(null);
        return Result.success(merchant);
    }

    @PutMapping("/profile")
    @Operation(summary = "修改店铺信息")
    public Result<String> updateProfile(@Valid @RequestBody MerchantUpdateDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            merchantService.updateProfile(merchantId, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<String> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            merchantService.updatePassword(merchantId, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/account")
    @Operation(summary = "注销账户")
    public Result<String> deleteAccount() {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            merchantService.deleteAccount(merchantId);
            StpUtil.logout();
            return Result.success("账户已注销");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

