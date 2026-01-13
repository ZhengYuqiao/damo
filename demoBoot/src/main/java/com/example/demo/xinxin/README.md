## 行信督办（后端示例）

### 1. MySQL 建表

执行：`src/main/resources/db/mysql/schema.sql`

### 2. 接口

- `GET /api/xinxin/supervision/switch`：查询开关
- `POST /api/xinxin/supervision/switch`：开启/关闭（仅总行）
  - Header：`X-Org-Level=HQ`，`X-User-Id=<操作人>`
  - Body：`{"enabled": true}` 或 `{"enabled": false}`
- `POST /api/xinxin/supervision/remind/run?month=YYYYMM`：手工触发提醒（仅总行）

### 3. 定时任务

配置项：

- `xinxin.supervision.cron`：默认每月 1 号 9 点执行
- `xinxin.supervision.target-month-offset`：默认提醒上月（-1）

