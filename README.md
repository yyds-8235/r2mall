# 乡村振兴电商平台

一个基于Spring Boot的电商平台，包含用户端和商家端，支持商品交易、订单管理和实时客服聊天功能。

## 技术栈

- **后端框架**: Spring Boot 3.5.6
- **数据库**: MySQL 8.0+
- **认证框架**: Sa-Token 1.37.0
- **数据访问层**: MyBatis-Plus 3.5.10
- **实时通信**: WebSocket
- **API文档**: SpringDoc OpenAPI 3 (Swagger)
- **密码加密**: Spring Security Crypto

## 核心功能

### 用户端
- 用户注册/登录
- 个人信息管理（头像、性别、出生日期）
- 收货地址管理
- 商品浏览/搜索（支持价格排序）
- 创建订单
- 模拟支付
- 订单查看
- 与商家实时客服沟通

### 商家端
- 商家入驻/登录
- 店铺信息管理
- 商品发布
- 商品管理（上下架、编辑）
- 与用户实时客服沟通

## 数据库设计

项目包含6张核心表：

1. **用户表 (user)** - 存储用户基本信息
2. **商家表 (merchant)** - 存储商家/店铺信息
3. **商品表 (product)** - 存储商品信息
4. **收货地址表 (shipping_address)** - 存储用户收货地址
5. **订单主表 (order_info)** - 存储订单概要信息
6. **订单商品表 (order_item)** - 存储订单中的具体商品

## 快速开始

### 1. 数据库配置

执行SQL脚本创建数据库和表：

```bash
mysql -u root -p < sql/schema.sql
```

### 2. 修改配置文件

编辑 `src/main/resources/application.yaml`，修改数据库连接信息：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/r2mall?...
    username: root
    password: 你的密码
```

### 3. 运行项目

```bash
mvn spring-boot:run
```

或在IDE中直接运行 `R2mallApplication` 主类。

### 4. 访问接口文档

项目启动后访问：

- Swagger UI: http://localhost:8082/swagger-ui.html
- API Docs: http://localhost:8082/v3/api-docs

## API接口说明

### 认证接口 (`/api/auth`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 统一登录 | POST | `/login` | 用户/商家登录 |
| 用户注册 | POST | `/user/register` | 用户注册 |
| 商家入驻 | POST | `/merchant/register` | 商家注册 |
| 注销登录 | POST | `/logout` | 退出登录 |

### 用户端接口

#### 个人中心 (`/api/user`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取个人信息 | GET | `/profile` | 查询用户信息 |
| 修改个人信息 | PUT | `/profile` | 更新用户信息 |
| 修改密码 | PUT | `/password` | 修改密码 |
| 注销账户 | DELETE | `/account` | 删除账户 |

#### 收货地址 (`/api/user/addresses`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取地址列表 | GET | `/` | 查询所有地址 |
| 新增地址 | POST | `/` | 添加新地址 |
| 修改地址 | PUT | `/{id}` | 更新地址 |
| 删除地址 | DELETE | `/{id}` | 删除地址 |

#### 商品浏览 (`/api/user/products`) - 无需登录

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 浏览/搜索商品 | GET | `/` | 分页、搜索、排序 |
| 获取商品详情 | GET | `/{id}` | 查看商品详情 |

#### 订单管理 (`/api/user/orders`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 创建订单 | POST | `/create` | 创建新订单 |
| 模拟支付 | POST | `/{orderNo}/pay` | 支付订单 |
| 查看订单列表 | GET | `/` | 查询订单列表 |
| 查看订单详情 | GET | `/{orderNo}` | 查询订单详情 |

### 商家端接口

#### 商家中心 (`/api/merchant`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取商家信息 | GET | `/profile` | 查询商家信息 |
| 修改店铺信息 | PUT | `/profile` | 更新店铺信息 |
| 修改密码 | PUT | `/password` | 修改密码 |
| 注销账户 | DELETE | `/account` | 删除账户 |

#### 商品管理 (`/api/merchant/products`)

| 功能 | 方法 | 路径 | 描述 |
|------|------|------|------|
| 获取我的商品 | GET | `/` | 查询商家商品 |
| 上架新商品 | POST | `/` | 发布商品 |
| 编辑商品 | PUT | `/{id}` | 修改商品信息 |
| 上下架商品 | PUT | `/{id}/status` | 修改商品状态 |

## WebSocket客服聊天

### 连接地址

```
ws://localhost:8082/im/{from_role}/{to_id}
```

- `from_role`: 发起方角色，`user` 或 `merchant`
- `to_id`: 对方ID

### 示例

**用户(ID=101)连接商家(ID=20)**:
```
ws://localhost:8082/im/user/20
```

**商家(ID=20)连接用户(ID=101)**:
```
ws://localhost:8082/im/merchant/101
```

### 消息格式

```json
{
  "fromUserId": "101",
  "toUserId": "20",
  "messageType": "text",
  "content": "你好，请问这个商品还有货吗？",
  "timestamp": 1678886400000
}
```

## 使用示例

### 1. 用户注册

```bash
POST /api/auth/user/register
Content-Type: application/json

{
  "username": "testuser",
  "password": "123456"
}
```

### 2. 用户登录

```bash
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "testuser",
  "password": "123456",
  "type": "user"
}
```

### 3. 商家登录

```bash
POST /api/auth/login
Content-Type: application/json

{
  "loginId": "merchant001",
  "password": "123456",
  "type": "merchant"
}
```

### 4. 创建订单

```bash
POST /api/user/orders/create
Authorization: Bearer {token}
Content-Type: application/json

{
  "addressId": 1,
  "items": [
    {
      "productId": 1,
      "quantity": 2
    }
  ]
}
```

## 注意事项

1. **密码加密**: 所有密码使用BCrypt加密存储
2. **Token认证**: 登录后需在请求头中携带Token: `satoken: {token值}`
3. **订单快照**: 订单商品和地址信息采用快照设计，确保历史数据准确
4. **库存扣减**: 创建订单时自动扣减库存
5. **默认地址**: 用户可设置默认收货地址，新增默认地址时会自动取消其他默认地址

## 项目结构

```
r2mall/
├── src/main/java/com/example/r2mall/
│   ├── common/              # 通用类（统一返回结果）
│   ├── config/              # 配置类
│   ├── controller/          # 控制器层
│   ├── exception/           # 异常处理
│   ├── handler/             # WebSocket处理器
│   ├── interceptor/         # 拦截器
│   ├── mapper/              # MyBatis Mapper
│   ├── pojo/
│   │   ├── dto/            # 数据传输对象
│   │   ├── entity/         # 实体类
│   │   └── vo/             # 视图对象
│   ├── service/            # 服务层
│   │   └── impl/           # 服务实现
│   └── util/               # 工具类
├── src/main/resources/
│   └── application.yaml    # 配置文件
└── sql/
    └── schema.sql          # 数据库脚本
```

## 开发环境

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- IDE: IntelliJ IDEA 或 Eclipse

## 许可证

本项目仅供学习和个人使用。

