package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.OrderCreateDTO;
import com.example.r2mall.pojo.dto.OrderStatusUpdateDTO;
import com.example.r2mall.pojo.entity.OrderInfo;
import com.example.r2mall.pojo.vo.OrderDetailVO;
import com.example.r2mall.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 订单管理控制器
 */
@RestController
@RequestMapping("/api/user/orders")
@RequiredArgsConstructor
@Tag(name = "订单管理", description = "用户订单管理接口")
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "创建订单")
    public Result<String> createOrder(@Valid @RequestBody OrderCreateDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            String orderNo = orderService.createOrder(userId, dto);
            return Result.success(orderNo);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/{orderNo}/pay")
    @Operation(summary = "模拟支付")
    public Result<String> payOrder(@PathVariable String orderNo) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            orderService.payOrder(userId, orderNo);
            return Result.success("支付成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "查看订单列表")
    public Result<Page<OrderInfo>> getOrderList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long userId = StpUtil.getLoginIdAsLong();
        Page<OrderInfo> result = orderService.getOrderList(userId, page, size);
        return Result.success(result);
    }

    @GetMapping("/{orderNo}")
    @Operation(summary = "查看订单详情")
    public Result<OrderDetailVO> getOrderDetail(@PathVariable String orderNo) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            OrderDetailVO detail = orderService.getOrderDetail(userId, orderNo);
            return Result.success(detail);
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{orderNo}/status")
    @Operation(summary = "修改订单状态", description = "确认收货")
    public Result<String> updateOrderStatus(
            @PathVariable String orderNo,
            @RequestBody OrderStatusUpdateDTO dto) {
        try {
            Long userId = StpUtil.getLoginIdAsLong();
            orderService.updateOrderStatus(userId, orderNo, dto.getStatus());
            return Result.success("订单状态更新成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

