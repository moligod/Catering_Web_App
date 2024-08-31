package com.example.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
//反射json转换
public class ReflectionJsonUtils {
    //转换对象的
    public static String toJson(Object obj) {
        if (obj == null) {
            return "{}";
        }
        try {
            Map<String, String> dataMap = new LinkedHashMap<>();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                Object value = field.get(obj);
                dataMap.put(field.getName(), value != null ? escapeJsonString(value.toString()) : "null");
            }
            return "{" + dataMap.entrySet().stream()
                    .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                    .collect(Collectors.joining(", ")) + "}";
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Access error in toJson", e);
        }
    }

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
    //转换列表的
    // 新增方法，用于将List转换为JSON数组字符串
    public static String toJsonList(List<?> list) {
        if (list == null || list.isEmpty()) {
            return "[]";
        }

        StringBuilder jsonArray = new StringBuilder();
        jsonArray.append("["); // 开始JSON数组

        for (int i = 0; i < list.size(); i++) {
            // 对于列表中的每个对象，调用现有的toJson()方法
            String objJson = toJson(list.get(i));
            jsonArray.append(objJson);
            if (i < list.size() - 1) {
                jsonArray.append(", "); // 在元素之间添加逗号
            }
        }

        jsonArray.append("]"); // 结束JSON数组
        return jsonArray.toString();
    }
    //转换string的
    public static String toJsonString(String key, String value) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        dataMap.put(key, escapeJsonString(value));
        return "{" + dataMap.entrySet().stream()
                .map(entry -> "\"" + entry.getKey() + "\":\"" + entry.getValue() + "\"")
                .collect(Collectors.joining(", ")) + "}";
    }
}
