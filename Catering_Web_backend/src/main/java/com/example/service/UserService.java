package com.example.service;

import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;

import java.util.List;

public interface UserService {
     ResponseVO<UserVO> loginUser(UserDTO userDTO);
     ResponseVO<Void> registerUser(UserDTO userDTO);
     ResponseVO<Void> logouttoken(String token);
     //查询user列表
//     ResponseVO<UserVO> getUserList(int pageSize, int page);
}
