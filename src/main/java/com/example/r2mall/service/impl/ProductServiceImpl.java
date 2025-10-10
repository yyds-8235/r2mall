package com.example.r2mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.mapper.ProductMapper;
import com.example.r2mall.pojo.dto.ProductDTO;
import com.example.r2mall.pojo.entity.Product;
import com.example.r2mall.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商品服务实现类
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductMapper productMapper;

    @Override
    public void createProduct(Long merchantId, ProductDTO dto) {
        Product product = new Product();
        product.setMerchantId(merchantId);
        product.setName(dto.getName());
        product.setImage(dto.getImage());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());
        product.setStatus(dto.getStatus() != null ? dto.getStatus() : 1); // 默认上架
        productMapper.insert(product);
    }

    @Override
    public void updateProduct(Long merchantId, Long productId, ProductDTO dto) {
        // 验证商品是否属于当前商家
        Product existProduct = productMapper.selectById(productId);
        if (existProduct == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!existProduct.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权修改该商品");
        }

        Product product = new Product();
        product.setId(productId);
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getImage() != null) {
            product.setImage(dto.getImage());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getStock() != null) {
            product.setStock(dto.getStock());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getStatus() != null) {
            product.setStatus(dto.getStatus());
        }
        productMapper.updateById(product);
    }

    @Override
    public void updateProductStatus(Long merchantId, Long productId, Integer status) {
        // 验证商品是否属于当前商家
        Product existProduct = productMapper.selectById(productId);
        if (existProduct == null) {
            throw new RuntimeException("商品不存在");
        }
        if (!existProduct.getMerchantId().equals(merchantId)) {
            throw new RuntimeException("无权修改该商品");
        }

        Product product = new Product();
        product.setId(productId);
        product.setStatus(status);
        productMapper.updateById(product);
    }

    @Override
    public Page<Product> getMerchantProducts(Long merchantId, Integer page, Integer size) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Product::getMerchantId, merchantId);
        wrapper.orderByDesc(Product::getCreateTime);
        return productMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Page<Product> searchProducts(String keyword, String sortBy, Integer page, Integer size) {
        Page<Product> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Product> wrapper = new LambdaQueryWrapper<>();
        
        // 只查询上架的商品
        wrapper.eq(Product::getStatus, 1);
        
        // 关键词搜索
        if (keyword != null && !keyword.trim().isEmpty()) {
            wrapper.like(Product::getName, keyword);
        }
        
        // 价格排序
        if ("price_asc".equals(sortBy)) {
            wrapper.orderByAsc(Product::getPrice);
        } else if ("price_desc".equals(sortBy)) {
            wrapper.orderByDesc(Product::getPrice);
        } else {
            wrapper.orderByDesc(Product::getCreateTime);
        }
        
        return productMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public Product getProductById(Long productId) {
        return productMapper.selectById(productId);
    }
}

