package com.example.r2mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.r2mall.mapper.ShippingAddressMapper;
import com.example.r2mall.pojo.dto.AddressDTO;
import com.example.r2mall.pojo.entity.ShippingAddress;
import com.example.r2mall.service.ShippingAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 收货地址服务实现类
 */
@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final ShippingAddressMapper addressMapper;

    @Override
    public List<ShippingAddress> getAddressList(Long userId) {
        LambdaQueryWrapper<ShippingAddress> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShippingAddress::getUserId, userId);
        wrapper.orderByDesc(ShippingAddress::getIsDefault);
        wrapper.orderByDesc(ShippingAddress::getCreateTime);
        return addressMapper.selectList(wrapper);
    }

    @Override
    public ShippingAddress getAddressById(Long id) {
        return addressMapper.selectById(id);
    }

    @Override
    @Transactional
    public void addAddress(Long userId, AddressDTO dto) {
        // 如果设置为默认地址，先将其他地址设置为非默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaUpdateWrapper<ShippingAddress> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ShippingAddress::getUserId, userId);
            updateWrapper.set(ShippingAddress::getIsDefault, 0);
            addressMapper.update(null, updateWrapper);
        }

        ShippingAddress address = new ShippingAddress();
        address.setUserId(userId);
        address.setRecipientName(dto.getRecipientName());
        address.setPhone(dto.getPhone());
        address.setAddress(dto.getAddress());
        address.setIsDefault(dto.getIsDefault() != null ? dto.getIsDefault() : 0);
        addressMapper.insert(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long userId, Long addressId, AddressDTO dto) {
        // 验证地址是否属于当前用户
        ShippingAddress existAddress = addressMapper.selectById(addressId);
        if (existAddress == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            throw new RuntimeException("无权修改该地址");
        }

        // 如果设置为默认地址，先将其他地址设置为非默认
        if (dto.getIsDefault() != null && dto.getIsDefault() == 1) {
            LambdaUpdateWrapper<ShippingAddress> updateWrapper = new LambdaUpdateWrapper<>();
            updateWrapper.eq(ShippingAddress::getUserId, userId);
            updateWrapper.ne(ShippingAddress::getId, addressId);
            updateWrapper.set(ShippingAddress::getIsDefault, 0);
            addressMapper.update(null, updateWrapper);
        }

        ShippingAddress address = new ShippingAddress();
        address.setId(addressId);
        if (dto.getRecipientName() != null) {
            address.setRecipientName(dto.getRecipientName());
        }
        if (dto.getPhone() != null) {
            address.setPhone(dto.getPhone());
        }
        if (dto.getAddress() != null) {
            address.setAddress(dto.getAddress());
        }
        if (dto.getIsDefault() != null) {
            address.setIsDefault(dto.getIsDefault());
        }
        addressMapper.updateById(address);
    }

    @Override
    public void deleteAddress(Long userId, Long addressId) {
        // 验证地址是否属于当前用户
        ShippingAddress existAddress = addressMapper.selectById(addressId);
        if (existAddress == null) {
            throw new RuntimeException("地址不存在");
        }
        if (!existAddress.getUserId().equals(userId)) {
            throw new RuntimeException("无权删除该地址");
        }

        addressMapper.deleteById(addressId);
    }
}

