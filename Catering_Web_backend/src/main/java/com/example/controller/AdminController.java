package com.example.controller;

import com.example.config.DependencyManager;
import com.example.model.dto.MenuDTO;
import com.example.model.dto.UserDTO;
import com.example.model.entity.Menu;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.MenuService;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;
import com.example.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class AdminController implements HttpHandler {
    //创建cookie
    TokenInterceptor tokenInterceptor = new TokenInterceptor();
    private UserService userService;
    private MenuService menuService;

    public AdminController() {
        this.userService = DependencyManager.getUserService();
        this.menuService = DependencyManager.getMenuService();
    }
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
            if("/admin/Dishesadd".equals(path)){
                handleaddDisheslish(exchange);
            }
            if ("/admin/Dishesdelete".equals(path)){
                handledelectMenu(exchange);
                ResponseSender.sendJsonResponse(exchange, 200, "666", null);
            }
            if ("/admin/Dishesupdate".equals(path)){
                handledishesupdate(exchange);
            }
        } else if("GET".equals(exchange.getRequestMethod())){
            if (path.contains("/admin/getMenuList")) {
                handleviewUserslish(exchange);
            }
            if (path.contains("/admin/getMenupages")) {
                handlegetMenupages(exchange);
            }
        }else {
            handleMethodNotAllowed(exchange);
        }
    }

    private void handledishesupdate(HttpExchange exchange) throws IOException {
        // 确保请求方法是 POST
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> formData = MultipartFormDataParser.parse(exchange);
            // 将修改数据放到DTO内然后传入逻辑层
            MenuDTO menuDTO = new MenuDTO(formData.get("name"), formData.get("description"),formData.get("price"),formData.get("category"),formData.get("stock"));
            // 循环输出对象的所有变量
            System.out.println("菜品名称: " + menuDTO.getName());
            System.out.println("描述: " + menuDTO.getDescription());
            System.out.println("价格: " + menuDTO.getPrice());
            System.out.println("类别: " + menuDTO.getCategory());
            System.out.println("库存: " + menuDTO.getStock());
            System.out.println("ID："+ formData.get("id"));
            ResponseVO<Integer> responseVO = menuService.updateMenu(menuDTO,formData.get("id"));
            ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), null);
        } else {
            System.out.println("非正确请求方法");
        }
    }

    private void handledelectMenu(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            String id = MultipartFormDataParser.readRequestBody(exchange);
            // 循环输出对象的所有变量
            ResponseVO<Void> responseVO = menuService.deleteMenu(id);
            ResponseSender.sendResponse(exchange, responseVO.getStatusCode(), String.valueOf(responseVO.getData()));
        } else {
            System.out.println("非正确请求方法");
        }
    }

    //获取页数,只发总数，让前端处理页数
    private void handlegetMenupages(HttpExchange exchange) throws IOException{
        ResponseVO<Integer> responseVO = menuService.getMenulistCount();
        System.out.println("控制层页面总数"+responseVO.getData());
        ResponseSender.sendResponse(exchange, responseVO.getStatusCode(), String.valueOf(responseVO.getData()));
    }

    //解析token获取名称返回
    private void handlegetUserName(HttpExchange exchange) throws IOException {
        //获取cookie,然后解析username，并返回
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        String token = tokenInterceptor.getCookietoken(cookieHeaders);
        String username = JWTUtil.getUsernameFromToken(token);
        String json = ReflectionJsonUtils.toJsonString("username", username);
        ResponseSender.sendJsonResponse(exchange, 200, "获取成功", json);
    }
    //添加user到列表中
    private void handleaddDisheslish(HttpExchange exchange) throws IOException {
        // 确保请求方法是 POST
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            Map<String, String> formData = MultipartFormDataParser.parse(exchange);
            // 将新增数据放到DTO内然后传入逻辑层
            MenuDTO menuDTO = new MenuDTO(formData.get("name"), formData.get("description"),formData.get("price"),formData.get("category"),formData.get("stock"));
            // 循环输出对象的所有变量
            System.out.println("菜品名称: " + menuDTO.getName());
            System.out.println("描述: " + menuDTO.getDescription());
            System.out.println("价格: " + menuDTO.getPrice());
            System.out.println("类别: " + menuDTO.getCategory());
            System.out.println("库存: " + menuDTO.getStock());
            ResponseVO<Integer> responseVO = menuService.addMenu(menuDTO);
            ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), null);
        } else {
            System.out.println("非正确请求方法");
        }

    }
    //展示user列表在前端
    private void handleviewUserslish(HttpExchange exchange) throws IOException {
        // 读取并解析前端发送的数据，多部分表单数据 form-data/form表单等的键值对数据
        Map<String, String> formData = MultipartFormDataParser.parseQueryString(exchange.getRequestURI().getQuery());
        int page = Integer.parseInt(formData.get("page"));
        int pageSize = Integer.parseInt(formData.get("pagesize"));
        ResponseVO<List<Menu>> responseVO = menuService.getMenuList(pageSize, page);
        String json = ReflectionJsonUtils.toJsonList(responseVO.getData());
        ResponseSender.sendJsonResponse(exchange, responseVO.getStatusCode(), responseVO.getMessage(), json);
    }
    private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
        // 返回405页面
        String response = "不允许的请求方式";
        exchange.sendResponseHeaders(405, response.getBytes().length);
        exchange.getResponseBody().write(response.getBytes());
        exchange.close();
    }
}
