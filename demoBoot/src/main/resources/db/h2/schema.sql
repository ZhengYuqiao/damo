-- H2 (MySQL mode) schema for tests/local run
CREATE TABLE IF NOT EXISTS xinxin_supervision_switch (
  id INT PRIMARY KEY,
  enabled TINYINT NOT NULL,
  enabled_at TIMESTAMP NULL,
  enabled_by VARCHAR(64) NULL,
  updated_at TIMESTAMP NOT NULL,
  updated_by VARCHAR(64) NULL
);

CREATE TABLE IF NOT EXISTS xinxin_vip_admin (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  branch_code VARCHAR(32) NOT NULL,
  user_id VARCHAR(64) NOT NULL,
  user_name VARCHAR(64) NULL,
  enabled TINYINT NOT NULL DEFAULT 1,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_xinxin_vip_admin_branch ON xinxin_vip_admin(branch_code);
CREATE UNIQUE INDEX IF NOT EXISTS uk_xinxin_vip_admin_branch_user ON xinxin_vip_admin(branch_code, user_id);

CREATE TABLE IF NOT EXISTS xinxin_vip_alignment_status (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  branch_code VARCHAR(32) NOT NULL,
  month VARCHAR(6) NOT NULL,
  completed TINYINT NOT NULL DEFAULT 0,
  updated_at TIMESTAMP NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_xinxin_align_branch_month ON xinxin_vip_alignment_status(branch_code, month);
CREATE INDEX IF NOT EXISTS idx_xinxin_align_month_completed ON xinxin_vip_alignment_status(month, completed);

CREATE TABLE IF NOT EXISTS xinxin_message_outbox (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  biz_type VARCHAR(64) NOT NULL,
  receiver_user_id VARCHAR(64) NOT NULL,
  title VARCHAR(128) NOT NULL,
  content VARCHAR(1024) NOT NULL,
  status VARCHAR(32) NOT NULL,
  created_at TIMESTAMP NOT NULL
);
CREATE INDEX IF NOT EXISTS idx_xinxin_outbox_receiver ON xinxin_message_outbox(receiver_user_id);
CREATE INDEX IF NOT EXISTS idx_xinxin_outbox_status ON xinxin_message_outbox(status);

CREATE TABLE IF NOT EXISTS xinxin_supervision_reminder_record (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  month VARCHAR(6) NOT NULL,
  branch_code VARCHAR(32) NOT NULL,
  admin_user_id VARCHAR(64) NOT NULL,
  sent_at TIMESTAMP NOT NULL,
  message_id BIGINT NULL,
  status VARCHAR(32) NOT NULL,
  error_msg VARCHAR(512) NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_xinxin_remind_month_branch_admin
  ON xinxin_supervision_reminder_record(month, branch_code, admin_user_id);
