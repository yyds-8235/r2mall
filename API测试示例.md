# API测试示例

本文档提供了使用Postman或curl测试API的完整示例。

## 1. 用户注册

```bash
POST http://localhost:8082/api/auth/user/register
Content-Type: application/json

{
  "username": "zhangsan",
  "password": "123456"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "注册成功",
  "data": "注册成功"
}
```

## 2. 商家入驻

```bash
POST http://localhost:8082/api/auth/merchant/register
Content-Type: application/json

{
  "merchantNo": "M001",
  "password": "123456",
  "shopName": "农家乐小店"
}
```

## 3. 用户登录

```bash
POST http://localhost:8082/api/auth/login
Content-Type: application/json

{
  "loginId": "zhangsan",
  "password": "123456",
  "type": "user"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "tokenName": "satoken",
    "tokenValue": "5e0e1e0e-9c8e-4c8e-9c8e-5e0e1e0e9c8e",
    "isLogin": true,
    "loginId": 1,
    "loginType": "login",
    "tokenTimeout": 86400,
    "sessionTimeout": -1,
    "tokenSessionTimeout": -1,
    "tokenActiveTimeout": -1,
    "loginDevice": "default-device"
  }
}
```

**重要**: 保存返回的 `tokenValue`，后续请求需要在Header中携带。

## 4. 商家登录

```bash
POST http://localhost:8082/api/auth/login
Content-Type: application/json

{
  "loginId": "M001",
  "password": "123456",
  "type": "merchant"
}
```

## 5. 用户获取个人信息

```bash
GET http://localhost:8082/api/user/profile
satoken: {你的token值}
```

## 6. 用户修改个人信息

```bash
PUT http://localhost:8082/api/user/profile
satoken: {你的token值}
Content-Type: application/json

{
  "avatar": "https://example.com/avatar.jpg",
  "gender": 1,
  "dateOfBirth": "1990-01-01"
}
```

## 7. 新增收货地址

```bash
POST http://localhost:8082/api/user/addresses
satoken: {你的token值}
Content-Type: application/json

{
  "recipientName": "张三",
  "phone": "13800138000",
  "address": "北京市朝阳区某某街道123号",
  "isDefault": 1
}
```

## 8. 浏览商品（无需登录）

```bash
# 基本浏览
GET http://localhost:8082/api/user/products?page=1&size=10

# 搜索商品
GET http://localhost:8082/api/user/products?keyword=苹果&page=1&size=10

# 价格升序排序
GET http://localhost:8082/api/user/products?sortBy=price_asc&page=1&size=10

# 价格降序排序
GET http://localhost:8082/api/user/products?sortBy=price_desc&page=1&size=10
```

## 9. 获取商品详情（无需登录）

```bash
GET http://localhost:8082/api/user/products/1
```

## 10. 商家发布商品

```bash
POST http://localhost:8082/api/merchant/products
satoken: {商家的token值}
Content-Type: application/json

{
  "name": "有机苹果",
  "image": "https://example.com/apple.jpg",
  "price": 29.90,
  "stock": 100,
  "description": "新鲜有机苹果，香甜可口",
  "status": 1
}
```

## 11. 商家获取商品列表

```bash
GET http://localhost:8082/api/merchant/products?page=1&size=10
satoken: {商家的token值}
```

## 12. 商家修改商品

```bash
PUT http://localhost:8082/api/merchant/products/1
satoken: {商家的token值}
Content-Type: application/json

{
  "name": "有机红富士苹果",
  "price": 35.90,
  "stock": 80
}
```

## 13. 商家上下架商品

```bash
# 下架商品
PUT http://localhost:8082/api/merchant/products/1/status
satoken: {商家的token值}
Content-Type: application/json

{
  "status": 0
}

# 上架商品
PUT http://localhost:8082/api/merchant/products/1/status
satoken: {商家的token值}
Content-Type: application/json

{
  "status": 1
}
```

## 14. 创建订单

```bash
POST http://localhost:8082/api/user/orders/create
satoken: {用户的token值}
Content-Type: application/json

{
  "addressId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "ORD1697886400000A1B2C3D4"
}
```

## 15. 模拟支付

```bash
POST http://localhost:8082/api/user/orders/ORD1697886400000A1B2C3D4/pay
satoken: {用户的token值}
```

## 16. 查看订单列表

```bash
GET http://localhost:8082/api/user/orders?page=1&size=10
satoken: {用户的token值}
```

## 17. 查看订单详情

```bash
GET http://localhost:8082/api/user/orders/ORD1697886400000A1B2C3D4
satoken: {用户的token值}
```

## 18. 修改密码

```bash
PUT http://localhost:8082/api/user/password
satoken: {用户的token值}
Content-Type: application/json

{
  "oldPassword": "123456",
  "newPassword": "654321"
}
```

## 19. 注销登录

```bash
POST http://localhost:8082/api/auth/logout
satoken: {你的token值}
```

## WebSocket客服聊天测试

### 使用JavaScript测试

```javascript
// 用户(ID=1)连接到商家(ID=1)
const ws = new WebSocket('ws://localhost:8082/im/user/1');

ws.onopen = function() {
    console.log('WebSocket连接已建立');
    
    // 发送消息
    const message = {
        fromUserId: '1',
        toUserId: '1',
        messageType: 'text',
        content: '你好，这个商品还有货吗？',
        timestamp: Date.now()
    };
    
    ws.send(JSON.stringify(message));
};

ws.onmessage = function(event) {
    console.log('收到消息:', event.data);
    const msg = JSON.parse(event.data);
    console.log('消息内容:', msg.content);
};

ws.onerror = function(error) {
    console.error('WebSocket错误:', error);
};

ws.onclose = function() {
    console.log('WebSocket连接已关闭');
};
```

### 使用在线WebSocket测试工具

1. 访问 http://www.websocket-test.com/ 或类似工具
2. 输入连接地址: `ws://localhost:8082/im/user/1`
3. 点击"Connect"
4. 发送消息（JSON格式）:
```json
{
  "fromUserId": "1",
  "toUserId": "1",
  "messageType": "text",
  "content": "你好",
  "timestamp": 1697886400000
}
```

## Postman配置说明

### 设置环境变量

1. 在Postman中创建环境
2. 添加变量：
   - `base_url`: `http://localhost:8082`
   - `user_token`: 用户登录后的token
   - `merchant_token`: 商家登录后的token

### 使用变量

- URL: `{{base_url}}/api/user/profile`
- Header: `satoken: {{user_token}}`

### 自动保存Token

在登录接口的Tests标签中添加：

```javascript
var jsonData = pm.response.json();
if (jsonData.code === 200) {
    pm.environment.set("user_token", jsonData.data.tokenValue);
}
```

## 常见错误处理

### 1. 未登录
```json
{
  "code": 401,
  "message": "未登录",
  "data": null
}
```
解决: 在Header中添加 `satoken: {token值}`

### 2. Token过期
```json
{
  "code": 401,
  "message": "Token已过期",
  "data": null
}
```
解决: 重新登录获取新token

### 3. 无权访问
```json
{
  "code": 403,
  "message": "无权操作该资源",
  "data": null
}
```
解决: 使用正确的账号登录

### 4. 参数错误
```json
{
  "code": 400,
  "message": "参数错误",
  "data": null
}
```
解决: 检查请求参数是否正确

## 完整测试流程

### 用户端测试流程

1. 用户注册
2. 用户登录（获取token）
3. 查看个人信息
4. 修改个人信息
5. 添加收货地址
6. 浏览商品
7. 查看商品详情
8. 创建订单
9. 模拟支付
10. 查看订单列表
11. 查看订单详情

### 商家端测试流程

1. 商家入驻
2. 商家登录（获取token）
3. 查看商家信息
4. 修改店铺信息
5. 发布商品
6. 查看商品列表
7. 修改商品信息
8. 上下架商品

### WebSocket聊天测试流程

1. 用户和商家分别登录
2. 用户建立WebSocket连接到商家
3. 商家建立WebSocket连接到用户
4. 用户发送消息
5. 商家接收并回复消息
6. 双向实时通信

