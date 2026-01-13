package com.example.demo.xinxin.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public class VipAdminRepository {
    private final JdbcTemplate jdbcTemplate;

    public VipAdminRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Map<String, Object>> findEnabledAdmins() {
        return jdbcTemplate.queryForList(
                "SELECT branch_code, user_id, user_name " +
                        "FROM xinxin_vip_admin " +
                        "WHERE enabled = 1"
        );
    }

    /**
     * 查询某月需要提醒的管理员：该分行该月未完成（completed=0 或无记录）则提醒。
     */
    public List<Map<String, Object>> findAdminsToRemind(String month) {
        return jdbcTemplate.queryForList(
                "SELECT a.branch_code, a.user_id, a.user_name " +
                        "FROM xinxin_vip_admin a " +
                        "LEFT JOIN xinxin_vip_alignment_status s " +
                        "  ON s.branch_code = a.branch_code AND s.month = ? " +
                        "WHERE a.enabled = 1 AND (s.id IS NULL OR s.completed = 0)",
                month
        );
    }
}

