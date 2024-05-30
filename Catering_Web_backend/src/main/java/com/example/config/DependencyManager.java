package com.example.config;

import com.example.dao.TokenDAO;
import com.example.dao.UserDAO;
import com.example.dao.impl.TokenDAOImpl;
import com.example.dao.impl.UserDAOImpl;
import com.example.service.UserService;
import com.example.service.impl.UserServiceImpl;


public class DependencyManager {
    private static UserService userService;
    private static TokenDAO tokenDAO;

    public static void initialize() throws Exception {
        // 初始化数据库，并测试连接
        DatabaseConfig.testConnection();

        // 初始化 DAO 和 Service
        UserDAO userDao = new UserDAOImpl(DatabaseConfig.getConnection());
        userService = new UserServiceImpl(userDao);
        // 初始化token验证，在路由的时候用
        TokenDAOImpl tokenDAO = new TokenDAOImpl(DatabaseConfig.getConnection());
    }

    public static UserService getUserService() {
        return userService;
    }

    public static TokenDAO getTokenDAO() {
        return tokenDAO;
    }
}
