package com.example.service.impl;

import com.example.dao.UserDAO;
import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;
import com.example.util.JWTUtil;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public ResponseVO<UserVO> loginUser(UserDTO userDTO) {
        //获取指定用户的信息
        User user = userDAO.findByUsername(userDTO.getUsername());
        //user为空代表没找到该账号，如果密码一样则生成token保存然后返回
        if (user == null) {
            return new ResponseVO<>(401, "查无此账号", null);
        } else if(user.getPassword().equals(userDTO.getPassword())) {
            // 生成JWT令牌并存储到数据库
            String token = JWTUtil.generateToken(userDTO.getUsername());
            user.setJwtToken(token);
            userDAO.updateUser(user); // 更新用户信息，保存token
            System.out.println("登录成功! JWT令牌: " + token);
            //表示层的数据（名字，职位，token）
            UserVO userVO = new UserVO(user.getUsername(),user.getRole(),token);
            return new ResponseVO<>(200, "登录成功", userVO);
        }
        //在能查找到用户并且密码不相等的到时候只有这一个选择
        return new ResponseVO<>(401, "密码错误", null);
    }
}