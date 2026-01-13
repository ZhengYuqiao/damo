package com.example.demo.xinxin.job;

import com.example.demo.xinxin.service.SupervisionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SupervisionMonthlyJob {
    private static final Logger log = LoggerFactory.getLogger(SupervisionMonthlyJob.class);

    private final SupervisionService supervisionService;

    public SupervisionMonthlyJob(SupervisionService supervisionService) {
        this.supervisionService = supervisionService;
    }

    @Scheduled(cron = "${xinxin.supervision.cron}")
    public void run() {
        int sent = supervisionService.runMonthlyRemindIfEnabled();
        log.info("行信督办月度提醒执行完成，新增发送={}条", sent);
    }
}

