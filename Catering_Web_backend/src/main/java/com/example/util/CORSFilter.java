package com.example.util;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.util.List;
//前后端跨域工具
public class CORSFilter extends Filter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {

        String origin = exchange.getRequestHeaders().getFirst("Origin");
        // 允许全部域名向服务器发送跨域请求，如果上线需要改
        System.out.println("发送端地址："+origin);
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", origin); // 反射原始请求的域
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");
        exchange.getResponseHeaders().add("Access-Control-Allow-Credentials", "true"); // 允许带凭证的请求
        // 处理预检请求
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.getResponseHeaders().add("Access-Control-Max-Age", "3600"); // 预检请求的结果缓存最大时间
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        // Continue with the next filter or the actual request handler
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Adds CORS headers to the response";
    }
}
