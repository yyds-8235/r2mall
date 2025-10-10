package com.example.r2mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.r2mall.pojo.entity.OrderItem;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单商品表Mapper接口
 */
@Mapper
public interface OrderItemMapper extends BaseMapper<OrderItem> {
}

