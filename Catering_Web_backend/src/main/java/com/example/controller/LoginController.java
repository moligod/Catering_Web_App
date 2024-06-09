package com.example.controller;

import com.example.config.DependencyManager;
import com.example.model.dto.UserDTO;

import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;



import com.example.util.MultipartFormDataParser;
import com.example.util.ReflectionJsonUtils;
import com.example.util.ResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;
import java.net.HttpCookie;

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
        UserDTO userDTO = new UserDTO(formData.get("username"), formData.get("password"));

        System.out.println("DTO模型-账号"+userDTO.getUsername()+"密码"+userDTO.getPassword());
        // 使用解析后的 form-data 数据处理登录逻辑
        ResponseVO<UserVO> responseVO = userService.loginUser(userDTO);
        //如果Data的数据不是空就说明登录成功了，否则我不会在Data写东西，我只会在Message中写错误提示
        if (responseVO.getData() != null) {
            // 设置 JWT 到 Cookie
            setJwtCookie(exchange, responseVO.getData().getJwtToken());
            // 序列化json数据
            String json = ReflectionJsonUtils.toJson(responseVO.getData());
            ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), json);
        } else {
            ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), null);
        }

    }

    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        // 返回405页面
        String response = "不允许的请求方式";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
    //设置cookie
    private void setJwtCookie(HttpExchange exchange, String jwtToken) {
        HttpCookie cookie = new HttpCookie("jwtToken", jwtToken);
        cookie.setMaxAge(86400);
        //为true可以防止客户端不可访问该cookie（如果设置true记得给下面string.format末尾添加HttpOnly）
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        // 生成符合HTTP标准的Set-Cookie头部值
        String cookieHeader = String.format("Set-Cookie: %s=%s; Path=%s; Max-Age=%d;",
                cookie.getName(),
                cookie.getValue(),
                cookie.getPath(),
                cookie.getMaxAge());
        exchange.getResponseHeaders().add("Set-Cookie", cookieHeader);
    }

}
