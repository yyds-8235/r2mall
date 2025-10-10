-- 测试数据SQL
-- 注意：密码都是 "123456" 经过BCrypt加密后的值
-- BCrypt加密后的 "123456": $2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy

USE `r2mall`;

-- 插入测试用户
INSERT INTO `user` (`username`, `password`, `avatar`, `gender`, `date_of_birth`) VALUES
('zhangsan', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', 'https://via.placeholder.com/150', 1, '1990-01-01'),
('lisi', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', 'https://via.placeholder.com/150', 2, '1995-06-15'),
('wangwu', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', NULL, 0, NULL);

-- 插入测试商家
INSERT INTO `merchant` (`merchant_no`, `password`, `shop_name`, `avatar`) VALUES
('M001', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', '农家乐小店', 'https://via.placeholder.com/200'),
('M002', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', '山货特产店', 'https://via.placeholder.com/200'),
('M003', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iXTVh9Cy', '绿色有机蔬菜', 'https://via.placeholder.com/200');

-- 插入测试商品（商家1的商品）
INSERT INTO `product` (`merchant_id`, `name`, `image`, `price`, `stock`, `description`, `status`) VALUES
(1, '有机红富士苹果', 'https://via.placeholder.com/300', 35.90, 100, '来自山东烟台的有机红富士苹果，香甜可口，果肉细腻', 1),
(1, '新鲜鸡蛋', 'https://via.placeholder.com/300', 25.00, 200, '农家散养土鸡蛋，营养丰富', 1),
(1, '有机蔬菜礼盒', 'https://via.placeholder.com/300', 68.00, 50, '包含多种时令有机蔬菜，新鲜配送', 1);

-- 插入测试商品（商家2的商品）
INSERT INTO `product` (`merchant_id`, `name`, `image`, `price`, `stock`, `description`, `status`) VALUES
(2, '野生核桃', 'https://via.placeholder.com/300', 88.00, 80, '大别山野生核桃，营养价值高', 1),
(2, '土蜂蜜', 'https://via.placeholder.com/300', 128.00, 30, '纯正土蜂蜜，无添加', 1),
(2, '山楂干', 'https://via.placeholder.com/300', 32.00, 120, '天然晾晒，酸甜可口', 1);

-- 插入测试商品（商家3的商品）
INSERT INTO `product` (`merchant_id`, `name`, `image`, `price`, `stock`, `description`, `status`) VALUES
(3, '有机西红柿', 'https://via.placeholder.com/300', 15.90, 150, '有机种植，无农药残留', 1),
(3, '有机黄瓜', 'https://via.placeholder.com/300', 12.50, 200, '脆嫩多汁，口感清爽', 1),
(3, '有机菠菜', 'https://via.placeholder.com/300', 8.90, 100, '富含铁元素，营养丰富', 1),
(3, '有机生菜', 'https://via.placeholder.com/300', 9.90, 80, '适合做沙拉，新鲜脆嫩', 0);

-- 插入测试收货地址（用户1的地址）
INSERT INTO `shipping_address` (`user_id`, `recipient_name`, `phone`, `address`, `is_default`) VALUES
(1, '张三', '13800138001', '北京市朝阳区某某街道123号', 1),
(1, '张三', '13800138001', '北京市海淀区中关村大街456号', 0);

-- 插入测试收货地址（用户2的地址）
INSERT INTO `shipping_address` (`user_id`, `recipient_name`, `phone`, `address`, `is_default`) VALUES
(2, '李四', '13800138002', '上海市浦东新区陆家嘴环路789号', 1);

-- 插入测试订单（用户1的订单）
INSERT INTO `order_info` (`order_no`, `user_id`, `total_amount`, `shipping_address`, `status`, `delivery_time`, `payment_time`) VALUES
('ORD20231001000001', 1, 97.80, '张三 13800138001 北京市朝阳区某某街道123号', 1, DATE_ADD(NOW(), INTERVAL 3 DAY), NOW()),
('ORD20231002000001', 1, 153.90, '张三 13800138001 北京市朝阳区某某街道123号', 0, DATE_ADD(NOW(), INTERVAL 3 DAY), NULL);

-- 插入订单商品（订单1的商品）
INSERT INTO `order_item` (`order_no`, `product_id`, `product_name`, `product_image`, `price`, `quantity`) VALUES
('ORD20231001000001', 1, '有机红富士苹果', 'https://via.placeholder.com/300', 35.90, 2),
('ORD20231001000001', 3, '有机蔬菜礼盒', 'https://via.placeholder.com/300', 25.00, 1);

-- 插入订单商品（订单2的商品）
INSERT INTO `order_item` (`order_no`, `product_id`, `product_name`, `product_image`, `price`, `quantity`) VALUES
('ORD20231002000001', 4, '野生核桃', 'https://via.placeholder.com/300', 88.00, 1),
('ORD20231002000001', 5, '土蜂蜜', 'https://via.placeholder.com/300', 128.00, 1);

-- 查询验证
SELECT '=== 用户列表 ===' AS '';
SELECT id, username, gender, date_of_birth, create_time FROM user;

SELECT '=== 商家列表 ===' AS '';
SELECT id, merchant_no, shop_name, create_time FROM merchant;

SELECT '=== 商品列表 ===' AS '';
SELECT p.id, p.name, p.price, p.stock, p.status, m.shop_name 
FROM product p 
LEFT JOIN merchant m ON p.merchant_id = m.id
ORDER BY p.id;

SELECT '=== 收货地址列表 ===' AS '';
SELECT sa.id, u.username, sa.recipient_name, sa.phone, sa.address, sa.is_default 
FROM shipping_address sa
LEFT JOIN user u ON sa.user_id = u.id
ORDER BY sa.id;

SELECT '=== 订单列表 ===' AS '';
SELECT o.order_no, u.username, o.total_amount, o.status, o.create_time
FROM order_info o
LEFT JOIN user u ON o.user_id = u.id
ORDER BY o.id;

