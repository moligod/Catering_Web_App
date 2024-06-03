package com.example.util;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;
//反射json转换
public class ReflectionJsonUtils {

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
}
