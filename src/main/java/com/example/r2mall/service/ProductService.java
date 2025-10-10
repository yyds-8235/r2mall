package com.example.r2mall.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.pojo.dto.ProductDTO;
import com.example.r2mall.pojo.entity.Product;

/**
 * 商品服务接口
 */
public interface ProductService {

    /**
     * 商家发布商品
     */
    void createProduct(Long merchantId, ProductDTO dto);

    /**
     * 商家更新商品
     */
    void updateProduct(Long merchantId, Long productId, ProductDTO dto);

    /**
     * 商家更新商品状态（上下架）
     */
    void updateProductStatus(Long merchantId, Long productId, Integer status);

    /**
     * 商家获取自己的商品列表
     */
    Page<Product> getMerchantProducts(Long merchantId, Integer page, Integer size);

    /**
     * 用户浏览/搜索商品（分页、搜索、排序）
     */
    Page<Product> searchProducts(String keyword, String sortBy, Integer page, Integer size);

    /**
     * 获取商品详情
     */
    Product getProductById(Long productId);
}

