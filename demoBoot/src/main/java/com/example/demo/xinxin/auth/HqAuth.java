package com.example.demo.xinxin.auth;

import javax.servlet.http.HttpServletRequest;

public final class HqAuth {
    private HqAuth() {}

    /**
     * 简化版“总行权限”校验：
     * - 生产可替换为 SSO/JWT/网关鉴权
     * - 当前实现：要求请求头 X-Org-Level=HQ
     */
    public static void requireHq(HttpServletRequest request) {
        String level = request.getHeader("X-Org-Level");
        if (level == null || !"HQ".equalsIgnoreCase(level.trim())) {
            throw new ForbiddenException("仅总行用户允许操作");
        }
    }

    public static String operatorUserId(HttpServletRequest request) {
        String userId = request.getHeader("X-User-Id");
        return userId == null ? "unknown" : userId.trim();
    }
}

