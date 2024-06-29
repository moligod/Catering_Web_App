package com.example.controller;

import com.example.config.DependencyManager;

import com.example.model.vo.ResponseVO;

import com.example.service.UserService;

import com.example.util.ResponseSender;
import com.example.util.TokenInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;


public class GlobalController implements HttpHandler {
    private UserService userService;
    TokenInterceptor tokenInterceptor = new TokenInterceptor();

    public GlobalController() {
        this.userService = DependencyManager.getUserService();
    }
    //处理通用的操作路径
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        //如果是post请求
        if ("POST".equals(exchange.getRequestMethod())) {
            if ("/logouttoken".equals(path)){
                logouttoken(exchange);
            }
        } else {
            handleMethodNotAllowed(exchange);
        }
    }

    private void logouttoken(HttpExchange exchange) throws IOException {
        //发送token给service层，返还ResponseVo给前端
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        String token = tokenInterceptor.getCookietoken(cookieHeaders);
        ResponseVO<Void> responseVO = userService.logouttoken(token);
        ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), null);
    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        // 返回405页面
        String response = "不允许的请求方式";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
