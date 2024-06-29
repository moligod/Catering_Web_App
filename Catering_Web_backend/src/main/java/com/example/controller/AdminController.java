package com.example.controller;

import com.example.util.JWTUtil;
import com.example.util.ReflectionJsonUtils;
import com.example.util.ResponseSender;
import com.example.util.TokenInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;

public class AdminController implements HttpHandler {
    //创建cookie
    TokenInterceptor tokenInterceptor = new TokenInterceptor();
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //获取用户发送的地址
        String path = exchange.getRequestURI().getPath();
        //如果是post请求
        if ("POST".equals(exchange.getRequestMethod())) {
            if ("/admin".equals(path)){
                ResponseSender.sendJsonResponse(exchange, 200, "666", null);
            }
            if ("/admin/getUserName".equals(path)) {
                //获取用户名，通过token来获得
                handlegetUserName(exchange);
            }
        } else {
            handleMethodNotAllowed(exchange);
        }
    }
    private void handlegetUserName(HttpExchange exchange) throws IOException {
        //获取cookie,然后解析username，并返回
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        String token = tokenInterceptor.getCookietoken(cookieHeaders);
        String username = JWTUtil.getUsernameFromToken(token);
        String json = ReflectionJsonUtils.toJsonString("username", username);
        ResponseSender.sendJsonResponse(exchange, 200, "666", json);
    }
    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        // 返回405页面
        String response = "不允许的请求方式";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
