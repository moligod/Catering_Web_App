package com.example.dao.impl;

import com.example.dao.UserDAO;
import com.example.model.entity.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAOImpl implements UserDAO {

    private Connection connection;

    public UserDAOImpl(Connection connection) {
        this.connection = connection;
    }
    //查找指定用户名信息
    @Override
    public User findByUsername(String username) {
        try {
            String sql = "SELECT * FROM users WHERE username = ?";
            //设置占位符参数
            PreparedStatement statement = connection.prepareStatement(sql);
            //第一个占位符为username
            statement.setString(1, username);
            //执行sql语句，并返回一个结果集
            ResultSet resultSet = statement.executeQuery();
            //结果集非空则转成实体返回回去
            if (resultSet.next()) {
                return mapResultSetToUser(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    //查找某些用户信息
    public List<User> finduserlist(int pageSize,int page){
        String sql = "SELECT * FROM users ORDER BY id LIMIT ? OFFSET ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, pageSize);
            stmt.setInt(2, (page - 1) * pageSize);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                // 处理每行数据
                return mapResultSetToUsers(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    //用ID找指定用户并更新信息
    @Override
    public void updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, role = ?, jwtToken = ?, updated_at = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.setString(4, user.getJwtToken());
            stmt.setTimestamp(5, java.sql.Timestamp.valueOf(user.getUpdatedAt()));
            stmt.setLong(6, user.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //新增用户信息
    @Override
    public boolean saveUser(User user) {
        String sql = "INSERT INTO users (username, password, role, created_at, updated_at) VALUES (?, ?, ?, ?, ?)";
        try {
            System.out.println(user.getCreatedAt()+" +++++++++++++ "+user.getUpdatedAt());
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(user.getCreatedAt()));
            statement.setTimestamp(5, java.sql.Timestamp.valueOf(user.getUpdatedAt()));
            //返回的整数表示执行 SQL 语句后受影响的行数（正整数：表示受影响的行数，0：表示没有行受影响。-1：表示执行了一个返回行计数的 SQL 语句）
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //把从数据库查询到的数据遍历到User实体类中
    private User mapResultSetToUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setUsername(resultSet.getString("username"));
        user.setPassword(resultSet.getString("password"));
        user.setRole(resultSet.getString("role"));
        user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
        user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
        user.setJwtToken(resultSet.getString("jwtToken"));
       return user;
    }
    //把从数据库查询到的数据遍历到User实体类中-多数据
    private List<User> mapResultSetToUsers(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        while (resultSet.next()) {
            User user = new User();
            user.setId(resultSet.getLong("id"));
            user.setUsername(resultSet.getString("username"));
            user.setPassword(resultSet.getString("password"));
            user.setRole(resultSet.getString("role"));
            user.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
            user.setUpdatedAt(resultSet.getTimestamp("updated_at").toLocalDateTime());
            user.setJwtToken(resultSet.getString("jwtToken"));
            users.add(user);
        }
        return users;
    }
    //删除token
    public int delecttoken(String token){
        String sql = "UPDATE users SET jwtToken = NULL WHERE jwtToken = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, token);
            // 执行DELETE语句
            int rowsAffected = stmt.executeUpdate();
            // 检查影响的行数，大于1删除成功，否则失败
            if (rowsAffected > 0) {
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
