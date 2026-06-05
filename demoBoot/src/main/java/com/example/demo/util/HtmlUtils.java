package com.example.demo.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.safety.Safelist;

/**
 * 富文本处理工具类
 * 用于在存储文章前去除 HTML 富文本标签，保留纯文本内容
 */
public class HtmlUtils {

    private HtmlUtils() {
    }

    /**
     * 去除 HTML 富文本标签，返回纯文本内容。
     *
     * <p>处理逻辑：
     * <ol>
     *   <li>使用 Jsoup 安全清洗，移除所有 HTML 标签及属性</li>
     *   <li>解码 HTML 实体（如 &amp;amp; → &amp;，&amp;nbsp; → 空格）</li>
     *   <li>合并多余的空白字符，去除首尾空白</li>
     * </ol>
     *
     * @param html 含有富文本标签的原始字符串，可为 null
     * @return 去除标签后的纯文本，若入参为空则返回空字符串
     */
    public static String stripHtml(String html) {
        if (html == null || html.trim().isEmpty()) {
            return "";
        }

        // 使用 Jsoup.clean 去除所有 HTML 标签（Safelist.none() 不保留任何标签）
        String cleaned = Jsoup.clean(html, Safelist.none());

        // Jsoup.clean 会保留 HTML 实体编码，再用 Jsoup.parse 解码为可读字符
        String decoded = Jsoup.parse(cleaned).text();

        // 将连续空白（含 &nbsp; 转换后的不间断空格）替换为单个普通空格
        return decoded.replaceAll("\\s+", " ").trim();
    }

    /**
     * 去除 HTML 富文本标签，同时保留指定的安全标签（白名单模式）。
     *
     * <p>适用于需要保留部分格式标签（如段落、加粗）再提取文本的场景。
     *
     * @param html      含有富文本标签的原始字符串，可为 null
     * @param safelist Jsoup 安全列表，决定保留哪些标签
     * @return 去除不在安全列表中的标签后的纯文本
     */
    public static String stripHtml(String html, Safelist safelist) {
        if (html == null || html.trim().isEmpty()) {
            return "";
        }

        String cleaned = Jsoup.clean(html, safelist);
        Document doc = Jsoup.parse(cleaned);
        // 以换行符拼接各块级元素的文本，保留基本段落结构
        doc.select("p, div, br, li").forEach(el -> el.after("\n"));
        String text = doc.text();

        return text.replaceAll("[ \\t]+", " ").trim();
    }
}
