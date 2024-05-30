package com.example.util.Discardedutil;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析 form-data 数据
 *
 * @param body 包含 for-data 数据的字符串
 * @return 一个包含解析后的键值对的 Map
 * @throws IOException 解析过程中可能抛出的异常
 */

public class FormDataParser {

    public static Map<String, String> parseFormData(String body) throws IOException {
        Map<String, String> formData = new HashMap<>();
        // 将字符串按 & 符号分割成键值对
        String[] pairs = body.split("&");
        for (String pair : pairs) {
            int idx = pair.indexOf("=");
            if (idx > 0 && idx < pair.length() - 1) {
                // 解码键和值，并放入 formData Map 中
                String key = URLDecoder.decode(pair.substring(0, idx), StandardCharsets.UTF_8.name());
                String value = URLDecoder.decode(pair.substring(idx + 1), StandardCharsets.UTF_8.name());
                formData.put(key, value);
            }
        }
        return formData;
    }
}
