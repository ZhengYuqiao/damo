-- MySQL 5.7/8.0：行信督办（建议库字符集 utf8mb4）
-- CREATE DATABASE demo_boot DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
-- USE demo_boot;

-- 1) 行信督办开关：单行配置（id=1）
CREATE TABLE IF NOT EXISTS xinxin_supervision_switch (
  id INT NOT NULL PRIMARY KEY,
  enabled TINYINT(1) NOT NULL,
  enabled_at DATETIME NULL,
  enabled_by VARCHAR(64) NULL,
  updated_at DATETIME NOT NULL,
  updated_by VARCHAR(64) NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 2) 分行一级分行要客管理员
CREATE TABLE IF NOT EXISTS xinxin_vip_admin (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  branch_code VARCHAR(32) NOT NULL,
  user_id VARCHAR(64) NOT NULL,
  user_name VARCHAR(64) NULL,
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL,
  updated_at DATETIME NOT NULL,
  KEY idx_xinxin_vip_admin_branch(branch_code),
  UNIQUE KEY uk_xinxin_vip_admin_branch_user(branch_code, user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 3) 要客标识对位完成情况（按月，month=YYYYMM）
CREATE TABLE IF NOT EXISTS xinxin_vip_alignment_status (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  branch_code VARCHAR(32) NOT NULL,
  month VARCHAR(6) NOT NULL,
  completed TINYINT(1) NOT NULL DEFAULT 0,
  updated_at DATETIME NOT NULL,
  UNIQUE KEY uk_xinxin_align_branch_month(branch_code, month),
  KEY idx_xinxin_align_month_completed(month, completed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 4) 消息出站（占位：短信/站内信/企微等通道可对接此表或替换实现）
CREATE TABLE IF NOT EXISTS xinxin_message_outbox (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  biz_type VARCHAR(64) NOT NULL,
  receiver_user_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  content VARCHAR(1024) NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at DATETIME NOT NULL,
  KEY idx_xinxin_outbox_receiver(receiver_user_id),
  KEY idx_xinxin_outbox_status(status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 5) 督办提醒发送记录（审计+幂等：同月同分行同管理员只发一次）
CREATE TABLE IF NOT EXISTS xinxin_supervision_reminder_record (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  month VARCHAR(6) NOT NULL,
  branch_code VARCHAR(32) NOT NULL,
  admin_user_id VARCHAR(64) NOT NULL,
  sent_at DATETIME NOT NULL,
  message_id BIGINT NULL,
  status VARCHAR(32) NOT NULL,
  error_msg VARCHAR(512) NULL,
  UNIQUE KEY uk_xinxin_remind_month_branch_admin(month, branch_code, admin_user_id),
  KEY idx_xinxin_remind_month(month),
  KEY idx_xinxin_remind_branch(branch_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- 初始化开关（默认关闭，保证有 id=1）
INSERT INTO xinxin_supervision_switch (id, enabled, enabled_at, enabled_by, updated_at, updated_by)
VALUES (1, 0, NULL, NULL, NOW(), 'system')
ON DUPLICATE KEY UPDATE updated_at = VALUES(updated_at), updated_by = VALUES(updated_by);
