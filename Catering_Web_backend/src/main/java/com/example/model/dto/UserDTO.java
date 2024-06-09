package com.example.model.dto;

public class UserDTO {
    private String username;
    private String password;
    private String role;
    private String jwtToken;
    private String reigsterCDK;
    //登录的时候用
    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //注册的时候用
    public UserDTO(String username, String password, String role, String reigsterCDK) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.reigsterCDK = reigsterCDK;
    }

    public String getReigsterCDK() { return reigsterCDK; }

    public void setReigsterCDK(String reigsterCDK) { this.reigsterCDK = reigsterCDK; }

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
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
}
