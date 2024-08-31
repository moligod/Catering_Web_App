package com.example.util;

import com.sun.net.httpserver.HttpExchange;
import com.sun.source.tree.ImportTree;

import javax.print.DocFlavor;
import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

//分析前端发送的请求，映射对对应的map类
public class MultipartFormDataParser {

    /**
     * 根据请求的 Content-Type 自动解析表单数据
     *
     * @param exchange HttpExchange 对象，包含请求数据
     * @return 解析后的表单数据，存储在 Map 中
     * @throws IOException 如果解析过程中发生 IO 异常
     */
    public static Map<String, String> parse(HttpExchange exchange) throws IOException {
        String contentType = exchange.getRequestHeaders().getFirst("Content-Type");
        System.out.println("提交的类型："+contentType);
        if (contentType != null) {
            // 处理 application/x-www-form-urlencoded 类型的表单数据
            if (contentType.contains("application/x-www-form-urlencoded")) {
                return parseFormUrlEncoded(exchange);
            }
            // 处理 multipart/form-data 类型的表单数据
            else if (contentType.contains("multipart/form-data")) {
                String boundary = contentType.split("boundary=")[1];
                return parseMultipartFormData(exchange.getRequestBody(), boundary);
            }
            else if (contentType.contains("text/plain")) {
                System.out.println("text/plain类型，请调用本雷中的getRequestBody获取");
            }
        }
        return new HashMap<>();
    }

    /**
     * 解析 application/x-www-form-urlencoded 类型的表单数据
     *
     * @param exchange HttpExchange 对象，包含请求数据
     * @return 解析后的表单数据，存储在 Map 中
     * @throws IOException 如果解析过程中发生 IO 异常
     */
    private static Map<String, String> parseFormUrlEncoded(HttpExchange exchange) throws IOException {
        Map<String, String> formData = new HashMap<>();
        InputStream requestBody = exchange.getRequestBody();
        String body = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))
                .lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);

        for (String pair : body.split("&")) {
            String[] keyValue = pair.split("=");
            String key = URLDecoder.decode(keyValue[0], "UTF-8");
            String value = URLDecoder.decode(keyValue.length > 1 ? keyValue[1] : "", "UTF-8");
            formData.put(key, value);
        }
        return formData;
    }

    /**
     * 解析 multipart/form-data 类型的表单数据
     *
     * @param inputStream 输入流，包含表单数据
     * @param boundary    分隔符，用于分割不同的表单项
     * @return 解析后的表单数据，存储在 Map 中
     * @throws IOException 如果解析过程中发生 IO 异常
     */
    private static Map<String, String> parseMultipartFormData(InputStream inputStream, String boundary) throws IOException {
        Map<String, String> formData = new HashMap<>();

        byte[] boundaryBytes = ("--" + boundary).getBytes();
        byte[] endBoundaryBytes = ("--" + boundary + "--").getBytes();

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] dataBuffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = inputStream.read(dataBuffer)) != -1) {
            buffer.write(dataBuffer, 0, bytesRead);
        }

        byte[] allData = buffer.toByteArray();
        int boundaryIndex = indexOf(allData, boundaryBytes, 0);

        while (boundaryIndex >= 0) {
            int nextBoundaryIndex = indexOf(allData, boundaryBytes, boundaryIndex + boundaryBytes.length);
            if (nextBoundaryIndex == -1) {
                nextBoundaryIndex = indexOf(allData, endBoundaryBytes, boundaryIndex + boundaryBytes.length);
            }
            if (nextBoundaryIndex == -1) break;

            int headerEndIndex = indexOf(allData, "\r\n\r\n".getBytes(), boundaryIndex) + 4;
            String headers = new String(allData, boundaryIndex + boundaryBytes.length + 2, headerEndIndex - (boundaryIndex + boundaryBytes.length + 2), StandardCharsets.UTF_8);

            String name = extractHeaderValue(headers, "name");
            int dataStartIndex = headerEndIndex;
            int dataEndIndex = nextBoundaryIndex - 2;

            String value = new String(allData, dataStartIndex, dataEndIndex - dataStartIndex, StandardCharsets.UTF_8);
            formData.put(name, value);

            boundaryIndex = nextBoundaryIndex;
        }

        return formData;
    }

    /**
     * 查找指定字节数组在另一个字节数组中的位置
     *
     * @param outerArray  被查找的字节数组
     * @param smallerArray 要查找的字节数组
     * @param start       开始查找的位置
     * @return 字节数组在被查找数组中的位置，未找到则返回 -1
     */
    private static int indexOf(byte[] outerArray, byte[] smallerArray, int start) {
        for (int i = start; i < outerArray.length - smallerArray.length + 1; i++) {
            boolean found = true;
            for (int j = 0; j < smallerArray.length; j++) {
                if (outerArray[i + j] != smallerArray[j]) {
                    found = false;
                    break;
                }
            }
            if (found) return i;
        }
        return -1;
    }

    /**
     * 从请求头中提取指定键的值
     *
     * @param headers 请求头字符串
     * @param key     要提取的键
     * @return 键对应的值，未找到则返回 null
     */
    private static String extractHeaderValue(String headers, String key) {
        for (String header : headers.split("\r\n")) {
            if (header.contains(key + "=\"")) {
                int start = header.indexOf(key + "=\"") + (key + "=\"").length();
                int end = header.indexOf("\"", start);
                return header.substring(start, end);
            }
        }
        return null;
    }
    //分析get请求，格式page=1&pagesize=2
    public static Map<String, String> parseQueryString(String queryString) {
        Map<String, String> params = new HashMap<>();
        String[] pairs = queryString.split("&"); // 按"&"分割参数

        for (String pair : pairs) {
            String[] keyValue = pair.split("="); // 按"="分割键和值
            if (keyValue.length == 2) {
                params.put(keyValue[0], keyValue[1]);
            }
        }

        return params;
    }

    /**
     * 读取请求体并返回給请求体的字符串表示
     *
     * @param exchange HttpExchange 对象
     * @return 请求体的字符串表示
     * @throws IOException 读取请求体时可能抛出的异常
     */
    public static String readRequestBody(HttpExchange exchange) throws IOException {
        try (InputStream requestBody = exchange.getRequestBody();
             BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))) {

            // 使用 try-with-resources 语句自动关闭资源
            // 使用 Collectors.joining 来合并所有行成为一个单一的字符串
            return reader.lines().collect(Collectors.joining());
        }
        // try-with-resources 语句结束，requestBody 和 reader 都被自动关闭
    }
}