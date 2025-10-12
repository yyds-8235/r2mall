package com.example.r2mall.pojo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类
 */
@Data
@TableName("product")
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 商品编号 (主键ID)
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 所属商家ID
     */
    @TableField("merchant_id")
    private Long merchantId;

    /**
     * 商品名字
     */
    @TableField("name")
    private String name;

    /**
     * 商品类别（如：蔬菜水果、粮油调味、肉蛋禽类等）
     */
    @TableField("category")
    private String category;

    /**
     * 商品主图片URL
     */
    @TableField("image")
    private String image;

    /**
     * 商品价格
     */
    @TableField("price")
    private BigDecimal price;

    /**
     * 库存数量
     */
    @TableField("stock")
    private Integer stock;

    /**
     * 商品备注/描述
     */
    @TableField("description")
    private String description;

    /**
     * 商品状态 (0: 下架, 1: 上架)
     */
    @TableField("status")
    private Integer status;

    /**
     * 上架日期
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 信息最后更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}

