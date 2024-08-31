package com.example.dao;

import com.example.model.entity.User;

import java.util.List;

public interface UserDAO {
    User findByUsername(String username);
    void updateUser(User user);
    boolean saveUser(User user);
    List<User> finduserlist(int pageSize, int page);
    // 其他DAO操作
    //查找某用户对应的token
    int delecttoken(String token);
}
