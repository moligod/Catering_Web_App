package com.example.dao;

import com.example.model.entity.User;

public interface UserDAO {
    User findByUsername(String username);
    void updateUser(User user);
    boolean saveUser(User user);
    // 其他DAO操作
    //查找某用户对应的token
    int delecttoken(String token);
}
