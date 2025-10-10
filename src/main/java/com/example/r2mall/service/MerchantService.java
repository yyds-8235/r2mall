package com.example.r2mall.service;

import com.example.r2mall.pojo.dto.MerchantRegisterDTO;
import com.example.r2mall.pojo.dto.MerchantUpdateDTO;
import com.example.r2mall.pojo.dto.PasswordUpdateDTO;
import com.example.r2mall.pojo.entity.Merchant;

/**
 * 商家服务接口
 */
public interface MerchantService {

    /**
     * 商家注册（入驻）
     */
    void register(MerchantRegisterDTO dto);

    /**
     * 根据商家号查询商家
     */
    Merchant getByMerchantNo(String merchantNo);

    /**
     * 根据ID查询商家
     */
    Merchant getById(Long id);

    /**
     * 更新商家信息
     */
    void updateProfile(Long merchantId, MerchantUpdateDTO dto);

    /**
     * 修改密码
     */
    void updatePassword(Long merchantId, PasswordUpdateDTO dto);

    /**
     * 注销账户
     */
    void deleteAccount(Long merchantId);
}

