package com.example.demo.xinxin.repo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository
public class MessageOutboxRepository {
    private final JdbcTemplate jdbcTemplate;

    public MessageOutboxRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public long insert(String bizType, String receiverUserId, String title, String content) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO xinxin_message_outbox " +
                            "(biz_type, receiver_user_id, title, content, status, created_at) " +
                            "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, bizType);
            ps.setString(2, receiverUserId);
            ps.setString(3, title);
            ps.setString(4, content);
            ps.setString(5, "PENDING");
            ps.setTimestamp(6, now);
            return ps;
        }, keyHolder);
        Number key = keyHolder.getKey();
        return key == null ? -1L : key.longValue();
    }
}

