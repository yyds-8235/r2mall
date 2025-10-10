package com.example.r2mall.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 商家实体类
 */
@Data
@TableName("merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 商家号，用于登录
     */
    @TableField("merchant_no")
    private String merchantNo;

    /**
     * 密码（加密存储）
     */
    @TableField("password")
    private String password;

    /**
     * 店铺名字
     */
    @TableField("shop_name")
    private String shopName;

    /**
     * 商家/店铺头像URL
     */
    @TableField("avatar")
    private String avatar;

    /**
     * 商家入驻时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 信息最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

