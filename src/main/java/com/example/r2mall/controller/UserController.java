package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.PasswordUpdateDTO;
import com.example.r2mall.pojo.dto.UserUpdateDTO;
import com.example.r2mall.pojo.entity.User;
import com.example.r2mall.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 用户个人中心控制器
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Tag(name = "用户个人中心", description = "用户个人信息管理接口")
public class UserController {

    private final UserService userService;

    @GetMapping("/profile")
    @Operation(summary = "获取个人信息")
    public Result<User> getProfile() {
        Long userId = StpUtil.getLoginIdAsLong();
        User user = userService.getById(userId);
        // 不返回密码
        user.setPassword(null);
        return Result.success(user);
    }

    @PutMapping("/profile")
    @Operation(summary = "修改个人信息")
    public Result<String> updateProfile(@Valid @RequestBody UserUpdateDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            userService.updateProfile(userId, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/password")
    @Operation(summary = "修改密码")
    public Result<String> updatePassword(@Valid @RequestBody PasswordUpdateDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            userService.updatePassword(userId, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/account")
    @Operation(summary = "注销账户")
    public Result<String> deleteAccount() {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            userService.deleteAccount(userId);
            StpUtil.logout();
            return Result.success("账户已注销");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}
