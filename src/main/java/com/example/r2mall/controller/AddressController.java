package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.AddressDTO;
import com.example.r2mall.pojo.entity.ShippingAddress;
import com.example.r2mall.service.ShippingAddressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 收货地址控制器
 */
@RestController
@RequestMapping("/api/user/addresses")
@RequiredArgsConstructor
@Tag(name = "收货地址管理", description = "用户收货地址管理接口")
public class AddressController {

    private final ShippingAddressService addressService;

    @GetMapping
    @Operation(summary = "获取地址列表")
    public Result<List<ShippingAddress>> getAddressList() {
        Long userId = StpUtil.getLoginIdAsLong();
        List<ShippingAddress> list = addressService.getAddressList(userId);
        return Result.success(list);
    }

    @PostMapping
    @Operation(summary = "新增地址")
    public Result<String> addAddress(@Valid @RequestBody AddressDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            addressService.addAddress(userId, dto);
            return Result.success("添加成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "修改地址")
    public Result<String> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            addressService.updateAddress(userId, id, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除地址")
    public Result<String> deleteAddress(@PathVariable Long id) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            addressService.deleteAddress(userId, id);
            return Result.success("删除成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

