package com.example.util.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    public static void main(String[] args) {
        Properties props = new Properties();

        // 使用类加载器来读取资源文件
        try (InputStream input = DatabaseConnection.class.getClassLoader().getResourceAsStream("jdbc.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find jdbc.properties");
                return;
            }

            // 加载属性文件
            props.load(input);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 读取属性值
        String url = props.getProperty("db.url");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");

        System.out.println("Database URL: " + url);
        System.out.println("Database User: " + user);

        try {
            // 加载驱动程序
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 建立连接
            Connection conn = DriverManager.getConnection(url, user, password);

            // 打印信息，表明连接成功
            System.out.println("连接成功！");

            // 关闭连接
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
