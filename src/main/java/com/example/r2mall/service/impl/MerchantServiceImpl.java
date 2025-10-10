package com.example.r2mall.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.r2mall.mapper.MerchantMapper;
import com.example.r2mall.pojo.dto.MerchantRegisterDTO;
import com.example.r2mall.pojo.dto.MerchantUpdateDTO;
import com.example.r2mall.pojo.dto.PasswordUpdateDTO;
import com.example.r2mall.pojo.entity.Merchant;
import com.example.r2mall.service.MerchantService;
import com.example.r2mall.util.PasswordUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 商家服务实现类
 */
@Service
@RequiredArgsConstructor
public class MerchantServiceImpl implements MerchantService {

    private final MerchantMapper merchantMapper;

    @Override
    public void register(MerchantRegisterDTO dto) {
        // 检查商家号是否已存在
        Merchant existMerchant = getByMerchantNo(dto.getMerchantNo());
        if (existMerchant != null) {
            throw new RuntimeException("商家号已存在");
        }

        // 创建商家
        Merchant merchant = new Merchant();
        merchant.setMerchantNo(dto.getMerchantNo());
        merchant.setPassword(PasswordUtil.simpleEncode(dto.getPassword()));
        merchant.setShopName(dto.getShopName());
        merchantMapper.insert(merchant);
    }

    @Override
    public Merchant getByMerchantNo(String merchantNo) {
        LambdaQueryWrapper<Merchant> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Merchant::getMerchantNo, merchantNo);
        return merchantMapper.selectOne(wrapper);
    }

    @Override
    public Merchant getById(Long id) {
        return merchantMapper.selectById(id);
    }

    @Override
    public void updateProfile(Long merchantId, MerchantUpdateDTO dto) {
        Merchant merchant = new Merchant();
        merchant.setId(merchantId);
        if (dto.getShopName() != null) {
            merchant.setShopName(dto.getShopName());
        }
        if (dto.getAvatar() != null) {
            merchant.setAvatar(dto.getAvatar());
        }
        merchantMapper.updateById(merchant);
    }

    @Override
    public void updatePassword(Long merchantId, PasswordUpdateDTO dto) {
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new RuntimeException("商家不存在");
        }

        // 验证旧密码
        if (!PasswordUtil.simpleMatches(dto.getOldPassword(), merchant.getPassword())) {
            throw new RuntimeException("旧密码错误");
        }

        // 更新密码
        Merchant updateMerchant = new Merchant();
        updateMerchant.setId(merchantId);
        updateMerchant.setPassword(PasswordUtil.simpleEncode(dto.getNewPassword()));
        merchantMapper.updateById(updateMerchant);
    }

    @Override
    public void deleteAccount(Long merchantId) {
        merchantMapper.deleteById(merchantId);
    }
}

