MERGE INTO xinxin_supervision_switch (id, enabled, enabled_at, enabled_by, updated_at, updated_by)
KEY (id)
VALUES (1, 0, NULL, NULL, CURRENT_TIMESTAMP(), 'system');

-- 演示数据：两个分行各一个管理员
INSERT INTO xinxin_vip_admin(branch_code, user_id, user_name, enabled, created_at, updated_at) VALUES
('B001', 'vip_admin_b001', '分行B001要客管理员', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP()),
('B002', 'vip_admin_b002', '分行B002要客管理员', 1, CURRENT_TIMESTAMP(), CURRENT_TIMESTAMP());

-- 演示数据：上月 B001 未完成，B002 已完成
-- month 由业务代码计算，这里留空不插入；测试用例会插入
