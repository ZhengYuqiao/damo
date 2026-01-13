package com.example.demo.xinxin.repo;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;

@Repository
public class ReminderRecordRepository {
    private final JdbcTemplate jdbcTemplate;

    public ReminderRecordRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 幂等插入：如果唯一键冲突（同月同分行同管理员已发送）则返回 false。
     */
    public boolean insertIfAbsent(String month, String branchCode, String adminUserId, long messageId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try {
            jdbcTemplate.update(
                    "INSERT INTO xinxin_supervision_reminder_record " +
                            "(month, branch_code, admin_user_id, sent_at, message_id, status, error_msg) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    month, branchCode, adminUserId, now, messageId, "SENT", null
            );
            return true;
        } catch (DuplicateKeyException ex) {
            return false;
        }
    }

    public void insertFailed(String month, String branchCode, String adminUserId, String errorMsg) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        try {
            jdbcTemplate.update(
                    "INSERT INTO xinxin_supervision_reminder_record " +
                            "(month, branch_code, admin_user_id, sent_at, message_id, status, error_msg) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    month, branchCode, adminUserId, now, null, "FAILED", errorMsg
            );
        } catch (DuplicateKeyException ignore) {
            // 已存在就不再覆盖
        }
    }
}

