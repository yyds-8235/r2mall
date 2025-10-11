package com.example.r2mall.pojo.vo;

import lombok.Data;

/**
 * LoginSuccessVO
 */
@Data
public class LoginSuccessVO<T> {
    /**
     * 用户的访问令牌
     */
    private String token;

    /**
     * 存储具体的实体类对象，类型由泛型 T 决定
     */
    private T userInfo;
}
