package com.example.util;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class ResponseSender {

    /**
     * 发送响应
     *
     * @param exchange   HttpExchange 对象
     * @param statusCode HTTP 状态码
     * @param response   响应体字符串
     * @throws IOException 发送响应时可能抛出的异常
     */
    //正常发送（需手动编写json然后当response）
    public static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, response.getBytes().length);
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
    //带json的发送
    public static void sendJsonResponse(HttpExchange exchange, int statusCode, String message, Object data) throws IOException {
        // 创建一个统一的JSON格式响应体
        String jsonResponse = String.format(
                "{\"statusCode\": %d, \"message\": \"%s\", \"data\": %s}",
                statusCode,
                // JSON字符串转义工具方法
                escapeJsonString(message),
                data != null ? data.toString() : "null"
        );

        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");
        exchange.sendResponseHeaders(statusCode, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
        OutputStream os = exchange.getResponseBody();
        os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
        os.close();
    }
    //重定向时发送
    public static void sendRedirect(HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().set("Location", location);
        exchange.sendResponseHeaders(302, -1); // 302状态码用于重定向
        exchange.close();
    }
    // JSON字符串转义工具方法
    private static String escapeJsonString(String value) {
        return value.replace("\\", "\\\\").replace("\"", "\\\"").replace("\b", "\\b")
                .replace("\f", "\\f").replace("\n", "\\n").replace("\r", "\\r")
                .replace("\t", "\\t");
    }
}

