package com.example.Router;

import com.example.controller.AdminController;
import com.example.controller.LoginController;
import com.example.controller.RegisterController;
import com.example.util.CORSFilter;
import com.example.util.TokenInterceptor;
import com.sun.net.httpserver.HttpServer;
import java.util.Arrays;

//路由管理界面
public class Router {
    public static void configure(HttpServer server) {
        //登录
        server.createContext("/login", new LoginController()).getFilters().add(new CORSFilter());
        //注册
        server.createContext("/register", new RegisterController()).getFilters().add(new CORSFilter());
        //主界面
        server.createContext("/admin", new AdminController()).getFilters().addAll(Arrays.asList(new CORSFilter(), new TokenInterceptor()));
    }
}
