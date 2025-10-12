package com.example.r2mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.r2mall.mapper.OrderInfoMapper;
import com.example.r2mall.mapper.OrderItemMapper;
import com.example.r2mall.mapper.ProductMapper;
import com.example.r2mall.mapper.ShippingAddressMapper;
import com.example.r2mall.pojo.dto.OrderCreateDTO;
import com.example.r2mall.pojo.dto.OrderItemDTO;
import com.example.r2mall.pojo.entity.*;
import com.example.r2mall.pojo.vo.OrderDetailVO;
import com.example.r2mall.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * 订单服务实现类
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderInfoMapper orderInfoMapper;
    private final OrderItemMapper orderItemMapper;
    private final ProductMapper productMapper;
    private final ShippingAddressMapper addressMapper;

    @Override
    @Transactional
    public String createOrder(Long userId, OrderCreateDTO dto) {
        // 查询收货地址
        ShippingAddress address = addressMapper.selectById(dto.getAddressId());
        if (address == null) {
            throw new RuntimeException("收货地址不存在");
        }
        if (!address.getUserId().equals(userId)) {
            throw new RuntimeException("无权使用该收货地址");
        }

        // 计算总金额并验证商品
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productMapper.selectById(itemDTO.getProductId());
            if (product == null) {
                throw new RuntimeException("商品不存在");
            }
            if (product.getStatus() != 1) {
                throw new RuntimeException("商品已下架");
            }
            if (product.getStock() < itemDTO.getQuantity()) {
                throw new RuntimeException("商品库存不足");
            }
            BigDecimal itemTotal = product.getPrice().multiply(BigDecimal.valueOf(itemDTO.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        // 生成订单号
        String orderNo = "ORD" + System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        // 创建订单主表
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setOrderNo(orderNo);
        orderInfo.setUserId(userId);
        orderInfo.setTotalAmount(totalAmount);
        orderInfo.setShippingAddress(address.getRecipientName() + " " + address.getPhone() + " " + address.getAddress());
        orderInfo.setStatus(0); // 待支付
        orderInfo.setDeliveryTime(LocalDateTime.now().plusDays(3)); // 预计3天送达
        orderInfoMapper.insert(orderInfo);

        // 创建订单商品项
        for (OrderItemDTO itemDTO : dto.getItems()) {
            Product product = productMapper.selectById(itemDTO.getProductId());
            OrderItem orderItem = new OrderItem();
            orderItem.setOrderNo(orderNo);
            orderItem.setProductId(product.getId());
            orderItem.setProductName(product.getName());
            orderItem.setProductImage(product.getImage());
            orderItem.setPrice(product.getPrice());
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItemMapper.insert(orderItem);

            // 减少库存
            Product updateProduct = new Product();
            updateProduct.setId(product.getId());
            updateProduct.setStock(product.getStock() - itemDTO.getQuantity());
            productMapper.updateById(updateProduct);
        }

        return orderNo;
    }

    @Override
    @Transactional
    public void payOrder(Long userId, String orderNo) {
        // 查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!orderInfo.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }
        if (orderInfo.getStatus() != 0) {
            throw new RuntimeException("订单状态异常");
        }

        // 更新订单状态
        OrderInfo updateOrder = new OrderInfo();
        updateOrder.setId(orderInfo.getId());
        updateOrder.setStatus(1); // 已支付
        updateOrder.setPaymentTime(LocalDateTime.now());
        orderInfoMapper.updateById(updateOrder);
    }

    @Override
    public Page<OrderInfo> getOrderList(Long userId, Integer page, Integer size) {
        Page<OrderInfo> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getUserId, userId);
        wrapper.orderByDesc(OrderInfo::getCreateTime);
        return orderInfoMapper.selectPage(pageParam, wrapper);
    }

    @Override
    public OrderDetailVO getOrderDetail(Long userId, String orderNo) {
        // 查询订单主信息
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!orderInfo.getUserId().equals(userId)) {
            throw new RuntimeException("无权查看该订单");
        }

        // 查询订单商品列表
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderNo, orderNo);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);

        // 组装返回对象
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderInfo(orderInfo);
        vo.setItems(items);
        return vo;
    }

    @Override
    public Page<OrderInfo> getMerchantOrders(Long merchantId, Integer page, Integer size) {
        Page<OrderInfo> pageParam = new Page<>(page, size);

        // 查询包含该商家商品的订单
        // 首先查询该商家的所有商品ID
        LambdaQueryWrapper<Product> productWrapper = new LambdaQueryWrapper<>();
        productWrapper.eq(Product::getMerchantId, merchantId);
        productWrapper.select(Product::getId);
        List<Product> products = productMapper.selectList(productWrapper);

        if (products.isEmpty()) {
            // 如果商家没有商品，返回空列表
            return pageParam;
        }

        List<Long> productIds = products.stream()
                .map(Product::getId)
                .toList();

        // 查询包含这些商品的订单号
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.in(OrderItem::getProductId, productIds);
        itemWrapper.select(OrderItem::getOrderNo);
        List<OrderItem> orderItems = orderItemMapper.selectList(itemWrapper);

        if (orderItems.isEmpty()) {
            return pageParam;
        }

        List<String> orderNos = orderItems.stream()
                .map(OrderItem::getOrderNo)
                .distinct()
                .toList();

        // 查询这些订单
        LambdaQueryWrapper<OrderInfo> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.in(OrderInfo::getOrderNo, orderNos);
        orderWrapper.orderByDesc(OrderInfo::getCreateTime);

        return orderInfoMapper.selectPage(pageParam, orderWrapper);
    }

    @Override
    public OrderDetailVO getMerchantOrderDetail(Long merchantId, String orderNo) {
        // 查询订单主信息
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }

        // 验证该订单是否包含该商家的商品
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderNo, orderNo);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);

        boolean hasMerchantProduct = false;
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null && product.getMerchantId().equals(merchantId)) {
                hasMerchantProduct = true;
                break;
            }
        }

        if (!hasMerchantProduct) {
            throw new RuntimeException("无权查看该订单");
        }

        // 组装返回对象
        OrderDetailVO vo = new OrderDetailVO();
        vo.setOrderInfo(orderInfo);
        vo.setItems(items);
        return vo;
    }

    @Override
    @Transactional
    public void shipOrder(Long merchantId, String orderNo) {
        // 查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }

        // 验证订单状态
        if (orderInfo.getStatus() != 1) {
            throw new RuntimeException("订单状态不是待发货，无法发货");
        }

        // 验证该订单是否包含该商家的商品
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderNo, orderNo);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);

        boolean hasMerchantProduct = false;
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null && product.getMerchantId().equals(merchantId)) {
                hasMerchantProduct = true;
                break;
            }
        }

        if (!hasMerchantProduct) {
            throw new RuntimeException("无权操作该订单");
        }

        // 更新订单状态为已发货
        OrderInfo updateOrder = new OrderInfo();
        updateOrder.setId(orderInfo.getId());
        updateOrder.setStatus(2); // 已发货
        orderInfoMapper.updateById(updateOrder);
    }

    @Override
    @Transactional
    public void updateOrderAddress(Long merchantId, String orderNo, String newAddress) {
        // 查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }

        // 只有待发货的订单才能修改地址
        if (orderInfo.getStatus() != 1) {
            throw new RuntimeException("只有待发货的订单才能修改地址");
        }

        // 验证该订单是否包含该商家的商品
        LambdaQueryWrapper<OrderItem> itemWrapper = new LambdaQueryWrapper<>();
        itemWrapper.eq(OrderItem::getOrderNo, orderNo);
        List<OrderItem> items = orderItemMapper.selectList(itemWrapper);

        boolean hasMerchantProduct = false;
        for (OrderItem item : items) {
            Product product = productMapper.selectById(item.getProductId());
            if (product != null && product.getMerchantId().equals(merchantId)) {
                hasMerchantProduct = true;
                break;
            }
        }

        if (!hasMerchantProduct) {
            throw new RuntimeException("无权操作该订单");
        }

        // 更新订单地址
        OrderInfo updateOrder = new OrderInfo();
        updateOrder.setId(orderInfo.getId());
        updateOrder.setShippingAddress(newAddress);
        orderInfoMapper.updateById(updateOrder);
    }

    @Override
    public void updateOrderStatus(Long userId, String orderNo, Integer status) {
        // 查询订单
        LambdaQueryWrapper<OrderInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(OrderInfo::getOrderNo, orderNo);
        OrderInfo orderInfo = orderInfoMapper.selectOne(wrapper);

        if (orderInfo == null) {
            throw new RuntimeException("订单不存在");
        }
        if (!orderInfo.getUserId().equals(userId)) {
            throw new RuntimeException("无权操作该订单");
        }

        // 验证状态变更是否合法
        if (status == 3) { // 用户确认收货
            if (orderInfo.getStatus() != 2) {
                throw new RuntimeException("只有已发货的订单才能确认收货");
            }
        } else {
            throw new RuntimeException("不支持的订单状态变更");
        }

        // 更新订单状态
        OrderInfo updateOrder = new OrderInfo();
        updateOrder.setId(orderInfo.getId());
        updateOrder.setStatus(status);

        orderInfoMapper.updateById(updateOrder);

    }
}

