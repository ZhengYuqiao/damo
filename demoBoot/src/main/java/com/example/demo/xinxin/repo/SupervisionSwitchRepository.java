package com.example.demo.xinxin.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Map;

@Repository
public class SupervisionSwitchRepository {
    private final JdbcTemplate jdbcTemplate;

    public SupervisionSwitchRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public boolean isEnabled() {
        Integer enabled = jdbcTemplate.queryForObject(
                "SELECT enabled FROM xinxin_supervision_switch WHERE id = 1",
                Integer.class
        );
        return enabled != null && enabled == 1;
    }

    public Map<String, Object> getRow() {
        return jdbcTemplate.queryForMap("SELECT * FROM xinxin_supervision_switch WHERE id = 1");
    }

    public void initIfMissing() {
        jdbcTemplate.update(
                "INSERT INTO xinxin_supervision_switch (id, enabled, enabled_at, enabled_by, updated_at, updated_by) " +
                        "SELECT 1, 0, NULL, NULL, ?, ? " +
                        "WHERE NOT EXISTS (SELECT 1 FROM xinxin_supervision_switch WHERE id=1)",
                new Timestamp(System.currentTimeMillis()),
                "system"
        );
    }

    public void updateEnabled(boolean enabled, String operator) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (enabled) {
            jdbcTemplate.update(
                    "UPDATE xinxin_supervision_switch " +
                            "SET enabled=1, enabled_at=?, enabled_by=?, updated_at=?, updated_by=? " +
                            "WHERE id=1",
                    now, operator, now, operator
            );
        } else {
            jdbcTemplate.update(
                    "UPDATE xinxin_supervision_switch " +
                            "SET enabled=0, updated_at=?, updated_by=? " +
                            "WHERE id=1",
                    now, operator
            );
        }
    }
}

