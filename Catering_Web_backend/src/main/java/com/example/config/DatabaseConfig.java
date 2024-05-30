package com.example.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConfig {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;

    static {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            Properties properties = new Properties();
            if (input == null) {
                throw new RuntimeException("Sorry, unable to find jdbc.properties");
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
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    // 测试连接方法
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