package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.LoginDTO;
import com.example.r2mall.pojo.dto.MerchantRegisterDTO;
import com.example.r2mall.pojo.dto.UserRegisterDTO;
import com.example.r2mall.pojo.entity.Merchant;
import com.example.r2mall.pojo.entity.User;
import com.example.r2mall.pojo.vo.LoginSuccessVO;
import com.example.r2mall.service.MerchantService;
import com.example.r2mall.service.UserService;
import com.example.r2mall.util.PasswordUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 */
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "认证接口", description = "用户和商家的登录、注册、注销接口")
public class AuthController {

    private final UserService userService;
    private final MerchantService merchantService;

    @PostMapping("/login")
    @Operation(summary = "统一登录", description = "根据type字段区分用户或商家登录")
    public Result<LoginSuccessVO<?>> login(@Valid @RequestBody LoginDTO dto) {
        if ("user".equals(dto.getRole())) {
            // 用户登录
            User user = userService.getByUsername(dto.getLoginId());
            if (user == null) {
                return Result.error("用户名或密码错误");
            }
            if (!PasswordUtil.simpleMatches(dto.getPassword(), user.getPassword())) {
                return Result.error("用户名或密码错误");
            }

            // 登录，并在session中标记用户类型
            StpUtil.login(user.getId());
            StpUtil.getSession().set("userType", "user");
            StpUtil.getSession().set("userId", user.getId());

            // 返回 LoginSuccessVO<User>，它符合 LoginSuccessVO<?>
            LoginSuccessVO<User> loginSuccessVO = new LoginSuccessVO<>();
            loginSuccessVO.setToken(StpUtil.getTokenInfo().getTokenValue());
            loginSuccessVO.setUserInfo(user);
            return Result.success(loginSuccessVO);

        } else if ("merchant".equals(dto.getRole())) {
            // 商家登录
            Merchant merchant = merchantService.getByMerchantNo(dto.getLoginId());
            if (merchant == null) {
                return Result.error("商家号或密码错误");
            }
            if (!PasswordUtil.simpleMatches(dto.getPassword(), merchant.getPassword())) {
                return Result.error("商家号或密码错误");
            }

            // 登录，并在session中标记商家类型
            StpUtil.login(merchant.getId());
            StpUtil.getSession().set("userType", "merchant");
            StpUtil.getSession().set("merchantId", merchant.getId());

            // 返回 LoginSuccessVO<Merchant>，它符合 LoginSuccessVO<?>
            LoginSuccessVO<Merchant> loginSuccessVO = new LoginSuccessVO<>();
            loginSuccessVO.setToken(StpUtil.getTokenInfo().getTokenValue());
            loginSuccessVO.setUserInfo(merchant);
            return Result.success(loginSuccessVO);

        } else {
            return Result.error("登录类型不正确");
        }
    }

    @PostMapping("/user/register")
    @Operation(summary = "用户注册")
    public Result<String> userRegister(@Valid @RequestBody UserRegisterDTO dto) {
        try {
            userService.register(dto);
            return Result.success("注册成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/merchant/register")
    @Operation(summary = "商家入驻", description = "商家注册入驻平台")
    public Result<String> merchantRegister(@Valid @RequestBody MerchantRegisterDTO dto) {
        try {
            merchantService.register(dto);
            return Result.success("入驻成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    @Operation(summary = "注销登录")
    public Result<String> logout() {
        StpUtil.logout();
        return Result.success("注销成功");
    }
}

