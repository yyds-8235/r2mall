package com.example.r2mall.service;

import com.example.r2mall.pojo.dto.PasswordUpdateDTO;
import com.example.r2mall.pojo.dto.UserRegisterDTO;
import com.example.r2mall.pojo.dto.UserUpdateDTO;
import com.example.r2mall.pojo.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     */
    void register(UserRegisterDTO dto);

    /**
     * 根据用户名查询用户
     */
    User getByUsername(String username);

    /**
     * 根据ID查询用户
     */
    User getById(Long id);

    /**
     * 更新用户信息
     */
    void updateProfile(Long userId, UserUpdateDTO dto);

    /**
     * 修改密码
     */
    void updatePassword(Long userId, PasswordUpdateDTO dto);

    /**
     * 注销账户
     */
    void deleteAccount(Long userId);
}
