package com.example.handler;

import com.example.util.ResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;

public class NotFoundHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
//        String response = "404 Not Foundss";
//        ResponseSender.sendResponse(exchange, 404, response);
//        exchange.sendResponseHeaders(404, response.getBytes().length);
//        OutputStream os = exchange.getResponseBody();
//        os.write(response.getBytes());
//        os.close();
        exchange.getResponseHeaders().add("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        exchange.getResponseHeaders().add("Access-Control-Allow-Headers", "Content-Type, Authorization");

        String response = "404 Not Found";
        exchange.sendResponseHeaders(404, response.length());
        exchange.getResponseBody().write(response.getBytes());
        exchange.getResponseBody().close();
    }
}
