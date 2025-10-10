package com.example.r2mall.service;

import com.example.r2mall.pojo.dto.AddressDTO;
import com.example.r2mall.pojo.entity.ShippingAddress;
import java.util.List;

/**
 * 收货地址服务接口
 */
public interface ShippingAddressService {

    /**
     * 获取用户的所有收货地址
     */
    List<ShippingAddress> getAddressList(Long userId);

    /**
     * 根据ID获取收货地址
     */
    ShippingAddress getAddressById(Long id);

    /**
     * 新增收货地址
     */
    void addAddress(Long userId, AddressDTO dto);

    /**
     * 更新收货地址
     */
    void updateAddress(Long userId, Long addressId, AddressDTO dto);

    /**
     * 删除收货地址
     */
    void deleteAddress(Long userId, Long addressId);
}

