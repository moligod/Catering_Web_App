package com.example.model.entity;

//import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private Long id;
    private String username;
    private String password;
    private String role;
    private String jwtToken;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    // getters and setters

    public User() {
    }
    //创建新用户
    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
        // 初始化 createdAt和updatedAt 属性为当前时间
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
