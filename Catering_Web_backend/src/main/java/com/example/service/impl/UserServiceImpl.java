package com.example.service.impl;

import com.example.dao.UserDAO;
import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;
import com.example.util.JWTUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    private final String registercdk="moligod666";
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
    //注册账号
    @Override
    public ResponseVO<Void> registerUser(UserDTO userDTO) {
        //检查管理员注册码是否正确
        if (!userDTO.getReigsterCDK().equals(registercdk)){
            System.out.println("111");
            return new ResponseVO<>(401, "注册码错误,请联系管理员获取注册码", null);
        }
        // 角色集合
        Set<String> validRoles = new HashSet<>(Arrays.asList("chef", "waiter", "admin"));

        if (userDTO.getUsername().length() > 6 && userDTO.getPassword().length() > 6 && validRoles.contains(userDTO.getRole())) {
            if (userDAO.findByUsername(userDTO.getUsername())!=null){
                return new ResponseVO<>(401, "用户名已被注册", null);
            }
            System.out.println("222");
            User user = new User(userDTO.getUsername(),userDTO.getPassword(),userDTO.getRole());
            boolean Execution_result=userDAO.saveUser(user);
            if (Execution_result){
                return new ResponseVO<>(200, "登录成功", null);
            }
            System.out.println("执行到未知错误");
            return new ResponseVO<>(200, "未知错误", null);

        }
        System.out.println("333");
            return new ResponseVO<>(401, "提交信息格式错误", null);
    }
}