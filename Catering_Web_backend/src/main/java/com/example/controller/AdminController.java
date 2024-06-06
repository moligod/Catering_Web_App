package com.example.controller;

import com.example.util.ResponseSender;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.tools.jconsole.JConsoleContext;

import java.io.IOException;

public class AdminController implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        ResponseSender.sendJsonResponse(exchange, 200, "666", null);
    }
}
