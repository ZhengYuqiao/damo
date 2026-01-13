package com.example.demo.xinxin.controller;

import com.example.demo.xinxin.auth.HqAuth;
import com.example.demo.xinxin.dto.SupervisionSwitchUpdateRequest;
import com.example.demo.xinxin.service.SupervisionService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/xinxin/supervision")
public class SupervisionController {
    private final SupervisionService supervisionService;

    public SupervisionController(SupervisionService supervisionService) {
        this.supervisionService = supervisionService;
    }

    /**
     * 查询行信督办开关状态
     */
    @GetMapping("/switch")
    public Map<String, Object> getSwitch() {
        Map<String, Object> row = supervisionService.getSwitch();
        Map<String, Object> resp = new HashMap<>();
        resp.put("enabled", row.get("enabled"));
        resp.put("enabledAt", row.get("enabled_at"));
        resp.put("enabledBy", row.get("enabled_by"));
        resp.put("updatedAt", row.get("updated_at"));
        resp.put("updatedBy", row.get("updated_by"));
        return resp;
    }

    /**
     * 开启/关闭行信督办（仅总行）
     *
     * 前端：弹窗确认后调用此接口，enabled=true 表示开启，false 表示关闭。
     */
    @PostMapping("/switch")
    public Map<String, Object> updateSwitch(@RequestBody SupervisionSwitchUpdateRequest body, HttpServletRequest request) {
        HqAuth.requireHq(request);
        if (body == null || body.getEnabled() == null) {
            throw new IllegalArgumentException("enabled 不能为空");
        }
        String operator = HqAuth.operatorUserId(request);
        supervisionService.updateEnabled(body.getEnabled(), operator);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("enabled", supervisionService.isEnabled());
        return resp;
    }

    /**
     * 手工触发一次提醒（仅总行，便于联调/验收）
     * - month: 可选，格式 YYYYMM；不传则按配置 offset 自动计算
     */
    @PostMapping("/remind/run")
    public Map<String, Object> runOnce(@RequestParam(value = "month", required = false) String month,
                                       HttpServletRequest request) {
        HqAuth.requireHq(request);
        String target = (month == null || month.trim().isEmpty()) ? supervisionService.calcTargetMonth() : month.trim();
        int sent = supervisionService.runMonthlyRemind(target);
        Map<String, Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("month", target);
        resp.put("sent", sent);
        return resp;
    }
}

