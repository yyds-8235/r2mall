package com.example.r2mall.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.dto.ProductDTO;
import com.example.r2mall.pojo.dto.ProductStatusDTO;
import com.example.r2mall.pojo.entity.Product;
import com.example.r2mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商家商品管理控制器
 */
@RestController
@RequestMapping("/api/merchant/products")
@RequiredArgsConstructor
@Tag(name = "商家商品管理", description = "商家管理商品的接口")
public class MerchantProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "获取我的商品列表")
    public Result<Page<Product>> getMyProducts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Long merchantId = StpUtil.getLoginIdAsLong();
        Page<Product> result = productService.getMerchantProducts(merchantId, page, size);
        return Result.success(result);
    }

    @PostMapping
    @Operation(summary = "上架新商品")
    public Result<String> createProduct(@Valid @RequestBody ProductDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            productService.createProduct(merchantId, dto);
            return Result.success("发布成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "编辑商品信息")
    public Result<String> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            productService.updateProduct(merchantId, id, dto);
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }

    @PutMapping("/{id}/status")
    @Operation(summary = "上架/下架商品")
    public Result<String> updateProductStatus(@PathVariable Long id, @Valid @RequestBody ProductStatusDTO dto) {
        try {
            Long merchantId = StpUtil.getLoginIdAsLong();
            productService.updateProductStatus(merchantId, id, dto.getStatus());
            return Result.success("修改成功");
        } catch (Exception e) {
            return Result.error(e.getMessage());
        }
    }
}

