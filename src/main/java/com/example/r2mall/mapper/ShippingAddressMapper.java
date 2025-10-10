package com.example.r2mall.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.r2mall.pojo.entity.ShippingAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收货地址Mapper接口
 */
@Mapper
public interface ShippingAddressMapper extends BaseMapper<ShippingAddress> {
}

