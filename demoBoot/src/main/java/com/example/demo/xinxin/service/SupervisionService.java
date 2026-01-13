package com.example.demo.xinxin.service;

import com.example.demo.xinxin.repo.MessageOutboxRepository;
import com.example.demo.xinxin.repo.ReminderRecordRepository;
import com.example.demo.xinxin.repo.SupervisionSwitchRepository;
import com.example.demo.xinxin.repo.VipAdminRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

@Service
public class SupervisionService {
    private static final Logger log = LoggerFactory.getLogger(SupervisionService.class);

    private final SupervisionSwitchRepository switchRepository;
    private final VipAdminRepository vipAdminRepository;
    private final MessageOutboxRepository outboxRepository;
    private final ReminderRecordRepository recordRepository;

    @Value("${xinxin.supervision.target-month-offset:-1}")
    private int targetMonthOffset;

    public SupervisionService(
            SupervisionSwitchRepository switchRepository,
            VipAdminRepository vipAdminRepository,
            MessageOutboxRepository outboxRepository,
            ReminderRecordRepository recordRepository
    ) {
        this.switchRepository = switchRepository;
        this.vipAdminRepository = vipAdminRepository;
        this.outboxRepository = outboxRepository;
        this.recordRepository = recordRepository;
    }

    public Map<String, Object> getSwitch() {
        switchRepository.initIfMissing();
        return switchRepository.getRow();
    }

    public boolean isEnabled() {
        switchRepository.initIfMissing();
        return switchRepository.isEnabled();
    }

    public void updateEnabled(boolean enabled, String operator) {
        switchRepository.initIfMissing();
        switchRepository.updateEnabled(enabled, operator);
    }

    public String calcTargetMonth() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH, targetMonthOffset);
        return new SimpleDateFormat("yyyyMM").format(cal.getTime());
    }

    @Transactional
    public int runMonthlyRemindIfEnabled() {
        if (!isEnabled()) {
            log.info("xinxin督办开关关闭，跳过本次提醒");
            return 0;
        }
        return runMonthlyRemind(calcTargetMonth());
    }

    /**
     * 对指定月份，提醒“未对位完成”的分行要客管理员。
     * @return 实际新增发送条数（幂等：已发送过的不重复计入）
     */
    @Transactional
    public int runMonthlyRemind(String month) {
        List<Map<String, Object>> targets = vipAdminRepository.findAdminsToRemind(month);
        int sent = 0;
        for (Map<String, Object> row : targets) {
            String branchCode = String.valueOf(row.get("branch_code"));
            String userId = String.valueOf(row.get("user_id"));
            String userName = row.get("user_name") == null ? "" : String.valueOf(row.get("user_name"));
            try {
                String title = "行信督办提醒";
                String content = "【" + month + "】分行(" + branchCode + ")要客标识对位尚未完成，请尽快完成。"
                        + (userName.isEmpty() ? "" : "（" + userName + "）");
                long msgId = outboxRepository.insert("XINXIN_SUPERVISION", userId, title, content);
                boolean inserted = recordRepository.insertIfAbsent(month, branchCode, userId, msgId);
                if (inserted) {
                    sent++;
                }
            } catch (Exception ex) {
                log.error("发送督办提醒失败 month={}, branch={}, user={}", month, branchCode, userId, ex);
                recordRepository.insertFailed(month, branchCode, userId, truncate(ex.getMessage(), 500));
            }
        }
        return sent;
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        if (s.length() <= max) return s;
        return s.substring(0, max);
    }
}

