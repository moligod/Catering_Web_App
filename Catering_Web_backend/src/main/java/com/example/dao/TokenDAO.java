package com.example.dao;

import com.example.model.entity.User;

public interface TokenDAO {
    String getTokenByUsername(String username);
}
