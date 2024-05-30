package com.example.util.Discardedutil;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
//读取前端发来的数据，已经被MultipartFormDataParser代替
public class RequestBodyReader {

    /**
     * 读取请求体并返回給请求体的字符串表示
     *
     * @param exchange HttpExchange 对象
     * @return 请求体的字符串表示
     * @throws IOException 读取请求体时可能抛出的异常
     */
    public static String readRequestBody(HttpExchange exchange) throws IOException {
        InputStream requestBody = exchange.getRequestBody();
        return new BufferedReader(new InputStreamReader(requestBody, StandardCharsets.UTF_8))
                .lines()
                .reduce("", (accumulator, actual) -> accumulator + actual);
    }
}
