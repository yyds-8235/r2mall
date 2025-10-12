package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.OrderAddressUpdateDTO;
import com.example.r2mall.pojo.entity.OrderInfo;
import com.example.r2mall.pojo.vo.OrderDetailVO;
import com.example.r2mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商家订单管理控制器
 */
@RestController
@RequestMapping("/api/merchant/orders")
@RequiredArgsConstructor
@Tag(name = "商家订单管理", description = "商家管理订单的接口")
public class MerchantOrderController {

    private final OrderService orderService;

    @GetMapping
    @Operation(summary = "获取我的订单列表", description = "查询包含本商家商品的所有订单")
    public Result<Page<OrderInfo>> getMyOrders(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long merchantId = StpUtil.getLoginIdAsLong();
        Page<OrderInfo> result = orderService.getMerchantOrders(merchantId, page, size);
        return Result.success(result);
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "查看订单详情")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            OrderDetailVO detail = orderService.getMerchantOrderDetail(merchantId, orderNo);
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/ship")
    @Operation(summary = "发货", description = "将订单状态从待发货改为已发货")
    public Result<String> shipOrder(@PathVariable String orderNo) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            orderService.shipOrder(merchantId, orderNo);
            return Result.success("发货成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{orderNo}/address")
    @Operation(summary = "修改订单地址", description = "修改待发货订单的收货地址")
    public Result<String> updateOrderAddress(
            @PathVariable String orderNo,
            @Valid @RequestBody OrderAddressUpdateDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            orderService.updateOrderAddress(merchantId, orderNo, dto.getShippingAddress());
            return Result.success("地址修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

