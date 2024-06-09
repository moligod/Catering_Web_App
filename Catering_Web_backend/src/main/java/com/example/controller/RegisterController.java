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

import java.io.IOException;
import java.util.Map;

public class RegisterController implements HttpHandler {
    private UserService userService;

    public RegisterController() {
        this.userService = DependencyManager.getUserService();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        //获取用户发送的地址
        String path = exchange.getRequestURI().getPath();
        //如果是post请求
        if ("POST".equals(exchange.getRequestMethod())) {
            if ("/register".equals(path)) {
                handleregister(exchange);
            }
        } else {
            handleMethodNotAllowed(exchange);
        }
    }


    private void handleregister(HttpExchange exchange) throws IOException {
        // 读取并解析前端发送的数据，多部分表单数据 form-data/form表单等的键值对数据
        Map<String, String> formData = MultipartFormDataParser.parse(exchange);
        //存储数据
        UserDTO userDTO = new UserDTO(formData.get("username"), formData.get("password"), formData.get("role"),formData.get("registerCDK"));
        System.out.println("DTO模型-账号"+userDTO.getUsername()+"密码"+userDTO.getPassword()+"职业"+userDTO.getRole()+"注册码"+userDTO.getReigsterCDK());
        // 使用解析后的 form-data 数据处理登录逻辑
        ResponseVO<Void> responseVO = userService.registerUser(userDTO);
        //注册无论成功是否只传Message和状态码
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
