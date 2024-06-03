package com.example.util.Discardedutil;

import java.util.Map;
import java.util.stream.Collectors;

public class JsonUtils {

    // 创建JSON字符串从Map
    public static String createJsonFromMap(Map<String, String> dataMap) {
        if (dataMap == null || dataMap.isEmpty()) {
            return "{}";
        }
        String elements = dataMap.entrySet().stream()
                .map(entry -> String.format("\"%s\": \"%s\"", escapeJsonString(entry.getKey()), escapeJsonString(entry.getValue())))
                .collect(Collectors.joining(", "));
        return "{" + elements + "}";
    }

    // JSON字符串转义工具方法
    private static String escapeJsonString(String value) {
        if (value == null) return "";
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}
