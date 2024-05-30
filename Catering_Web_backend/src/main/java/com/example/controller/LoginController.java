package com.example.controller;

import com.example.config.DependencyManager;
import com.example.model.dto.UserDTO;

import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;


import com.example.util.MultipartFormDataParser;
import com.example.util.ResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.util.Map;

public class LoginController implements HttpHandler {
    private UserService userService;

    public LoginController() {
        this.userService = DependencyManager.getUserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //获取用户发送的地址
        String path = exchange.getRequestURI().getPath();
        //如果是post请求
        if ("POST".equals(exchange.getRequestMethod())) {
            if ("/login".equals(path)) {
                handleLogin(exchange);
            }
        } else {
            handleMethodNotAllowed(exchange);
        }
    }

    private void handleLogin(HttpExchange exchange) throws IOException {
        // 读取并解析前端发送的数据，多部分表单数据 form-data/form表单等的键值对数据
        Map<String, String> formData = MultipartFormDataParser.parse(exchange);
        //存储数据
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(formData.get("username"));
        userDTO.setPassword(formData.get("password"));

        System.out.println("DTO模型-账号"+userDTO.getUsername()+"密码"+userDTO.getPassword());
        // 使用解析后的 form-data 数据处理登录逻辑
        ResponseVO<UserVO> responseVO = userService.loginUser(userDTO);

        if (responseVO.getData() != null) {
            String jsonResponse = "{\"statusCode\":" + responseVO.getStatusCode() + ",\"message\":\"" + responseVO.getMessage() + "\",\"data\":{\"username\":\"" + responseVO.getData().getUsername() + "\",\"role\":\"" + responseVO.getData().getRole() + "\",\"jwtToken\":\"" + responseVO.getData().getJwtToken() + "\"}}";
            ResponseSender.sendResponse(exchange, responseVO.getStatusCode(), jsonResponse);
        } else {
            ResponseSender.sendResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage());
        }

    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        // 返回405页面
        String response = "不允许的请求方式";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }

    private void handleNotFound(HttpExchange exchange) throws IOException {
        // 返回404页面
        String response = "404 Not Found";
        exchange.sendResponseHeaders(404, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
