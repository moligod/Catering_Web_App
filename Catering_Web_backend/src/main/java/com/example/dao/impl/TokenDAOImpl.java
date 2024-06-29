package com.example.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//token是中间件所以不需要初始化
public class TokenDAOImpl {
    private static Connection connection;

    public TokenDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public static String getTokenByUsername(String username) {
        String sql = "SELECT jwtToken FROM users WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("jwtToken");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
