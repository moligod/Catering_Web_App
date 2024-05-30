package com.example.initializer;

import com.example.config.DatabaseConfig;
import com.example.config.DependencyManager;
import com.example.dao.UserDAO;
//import com.example.dao.impl.UserDaoImpl;
import com.example.dao.impl.TokenDAOImpl;
import com.example.dao.impl.UserDAOImpl;
import com.example.service.UserService;
import com.example.Router.Router;
//import com.example.service.impl.UserServiceImpl;
import com.example.service.impl.UserServiceImpl;
import com.example.util.CORSFilter;
import com.sun.net.httpserver.HttpServer;

import java.net.InetSocketAddress;

public class ServerInitializer {
    public static void initialize() throws Exception {
        // 初始化依赖管理器
        DependencyManager.initialize();

        // 设置 HTTP 服务器
        HttpServer server = HttpServer.create(new InetSocketAddress(8081), 0);

        Router.configure(server); // 配置路由
        server.setExecutor(null); // 使用默认的单线程执行器
        server.start();

        System.out.println("已启动服务器端口 8081");
    }
}
