package com.example.r2mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.r2mall.pojo.entity.OrderInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单主表Mapper接口
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfo> {
}

