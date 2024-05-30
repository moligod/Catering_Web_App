package com.example.service;

import com.example.model.dto.UserDTO;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;

public interface UserService {
     ResponseVO<UserVO> loginUser(UserDTO userDTO);
//    String generateToken(UserDTO userDTO);
//    boolean registerUser(UserDTO userDTO);
    // 其他业务操作
}
