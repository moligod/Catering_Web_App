package com.example.util;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.Filter.Chain;
import java.io.IOException;
import java.util.List;
import com.example.util.JWTUtil;

public class GlobalMiddleware extends Filter {

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        // Add CORS headers to the response
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        // Handle preflight requests
        if ("OPTIONS".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(204, -1); // No Content
            return;
        }

        // Token validation
        List<String> authHeaders = exchange.getRequestHeaders().get("Authorization");

        if (authHeaders != null && !authHeaders.isEmpty()) {
            String token = authHeaders.get(0).replace("Bearer ", "");
            if (JWTUtil.validateToken(token)) {
                chain.doFilter(exchange);
                return;
            }
        }

        // If token validation fails, redirect to /login
        if (!exchange.getRequestURI().getPath().equals("/login") && !exchange.getRequestURI().getPath().equals("/register")) {
            exchange.getResponseHeaders().set("Location", "/login");
            exchange.sendResponseHeaders(302, -1); // 302 status code for redirection
            exchange.close();
            return;
        }

        // Continue with the next filter or the actual request handler
        chain.doFilter(exchange);
    }

    @Override
    public String description() {
        return "Global middleware for CORS and token validation";
    }
}
