package com.example.Router;

import com.example.controller.LoginController;
import com.example.handler.NotFoundHandler;
import com.example.util.CORSFilter;
import com.example.util.TokenInterceptor;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;


public class Router {
    public static void configure(HttpServer server) {
        // 为服务器创建一个新的上下文，路径为 /login，处理器为 loginController，然后为这个上下文添加一个 TokenInterceptor 过滤器
        server.createContext("/login", new LoginController()).getFilters().addAll(Arrays.asList(new CORSFilter(), new TokenInterceptor()));
        // 捕获所有未匹配的请求，返回404
        // 添加默认的404处理器，捕获所有未匹配的请求
        HttpHandler notFoundHandler = new HttpHandler() {
            @Override
            public void handle(HttpExchange exchange) throws IOException {
                exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
                exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

                String response = "<html><body><h1>404 Not Found</h1></body></html>";
                exchange.sendResponseHeaders(404, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        };
        server.createContext("/", notFoundHandler).getFilters().add(new CORSFilter());
//        server.createC7ontext("/", new NotFoundHandler()).getFilters().add(new CORSFilter());
    }
}
