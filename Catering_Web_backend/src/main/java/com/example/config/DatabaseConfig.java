package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private final static String url;
    private final static String username;
    private final static String password;
    private final static String driver;

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                throw new RuntimeException("没找到jdbc配置文件");
            }
            properties.load(input);

            url = properties.getProperty("db.url");
            username = properties.getProperty("db.username");
            password = properties.getProperty("db.password");
            driver = properties.getProperty("jdbcName");

            Class.forName(driver);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load database configuration", e);
        }
    }
    //获取数据库连接管道,用来操作数据库的管道
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    // 测试连接数据库是否可以使用
    public static void testConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            System.out.println("连接成功！");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("MySQL驱动程序类找不到。");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("数据库连接失败：");
            e.printStackTrace();
        }
    }
}