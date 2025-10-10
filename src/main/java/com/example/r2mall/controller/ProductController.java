package com.example.r2mall.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.common.Result;
import com.example.r2mall.pojo.entity.Product;
import com.example.r2mall.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * 商品浏览控制器
 */
@RestController
@RequestMapping("/api/user/products")
@RequiredArgsConstructor
@Tag(name = "商品浏览", description = "用户浏览、搜索商品接口（无需登录）")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @Operation(summary = "浏览/搜索商品", description = "分页、搜索、排序商品列表，无需登录")
    public Result<Page<Product>> searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        Page<Product> result = productService.searchProducts(keyword, sortBy, page, size);
        return Result.success(result);
    }

    @GetMapping("/{id}")
    @Operation(summary = "获取商品详情", description = "无需登录")
    public Result<Product> getProductDetail(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return Result.error("商品不存在");
        }
        return Result.success(product);
    }
}

