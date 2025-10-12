-- 创建数据库
CREATE DATABASE IF NOT EXISTS `r2mall` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE `r2mall`;

-- 1. 用户表
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名，用于登录',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '用户头像URL',
  `gender` TINYINT(1) DEFAULT 0 COMMENT '性别 (0: 未知, 1: 男, 2: 女)',
  `date_of_birth` DATE DEFAULT NULL COMMENT '出生年月',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '账户创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 2. 商家表
DROP TABLE IF EXISTS `merchant`;
CREATE TABLE `merchant` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `merchant_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '商家号，用于登录',
  `password` VARCHAR(100) NOT NULL COMMENT '密码（加密存储）',
  `shop_name` VARCHAR(100) NOT NULL COMMENT '店铺名字',
  `avatar` VARCHAR(255) DEFAULT NULL COMMENT '商家/店铺头像URL',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '商家入驻时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商家表';

-- 3. 商品表
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '商品编号 (主键ID)',
  `merchant_id` BIGINT UNSIGNED NOT NULL COMMENT '所属商家ID',
  `name` VARCHAR(150) NOT NULL COMMENT '商品名字',
  `category` VARCHAR(50) NOT NULL COMMENT '商品类别（如：蔬菜水果、粮油调味、肉蛋禽类等）',
  `image` VARCHAR(255) NOT NULL COMMENT '商品主图片URL',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '商品价格',
  `stock` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '库存数量',
  `description` TEXT COMMENT '商品备注/描述',
  `status` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '商品状态 (0: 下架, 1: 上架)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '上架日期',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '信息最后更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_merchant_id` (`merchant_id`),
  KEY `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 4. 收货地址表
DROP TABLE IF EXISTS `shipping_address`;
CREATE TABLE `shipping_address` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `recipient_name` VARCHAR(50) NOT NULL COMMENT '收货人姓名',
  `phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `address` VARCHAR(255) NOT NULL COMMENT '详细收货地址',
  `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为默认地址 (0: 否, 1: 是)',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户收货地址表';

-- 5. 订单主表
DROP TABLE IF EXISTS `order_info`;
CREATE TABLE `order_info` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(64) NOT NULL UNIQUE COMMENT '订单编号 (业务唯一)',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '下单用户ID',
  `total_amount` DECIMAL(10, 2) NOT NULL COMMENT '实付款金额',
  `shipping_address` VARCHAR(255) NOT NULL COMMENT '收货地址（快照）',
  `status` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '订单状态 (0: 待支付, 1: 已支付/待发货, 2: 已发货/配送中)',
  `delivery_time` DATETIME COMMENT '预计送达时间',
  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
  `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单主表';

-- 6. 订单商品表
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_no` VARCHAR(64) NOT NULL COMMENT '订单编号',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `product_name` VARCHAR(150) NOT NULL COMMENT '商品名称（快照）',
  `product_image` VARCHAR(255) NOT NULL COMMENT '商品图片URL（快照）',
  `price` DECIMAL(10, 2) NOT NULL COMMENT '购买时单价（快照）',
  `quantity` INT UNSIGNED NOT NULL COMMENT '购买数量',
  PRIMARY KEY (`id`),
  KEY `idx_order_no` (`order_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单商品表';

-- 7. 聊天消息表
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `from_user_id` VARCHAR(50) NOT NULL COMMENT '发送者ID',
  `to_user_id` VARCHAR(50) NOT NULL COMMENT '接收者ID',
  `message_type` VARCHAR(20) NOT NULL DEFAULT 'text' COMMENT '消息类型',
  `content` TEXT NOT NULL COMMENT '消息内容',
  `timestamp` BIGINT NOT NULL COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_from_to` (`from_user_id`, `to_user_id`),
  KEY `idx_to_from` (`to_user_id`, `from_user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';

