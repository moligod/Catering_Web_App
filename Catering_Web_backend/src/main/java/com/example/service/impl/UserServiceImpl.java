package com.example.service.impl;

import com.example.config.Contants;
import com.example.dao.UserDAO;
import com.example.model.dto.UserDTO;
import com.example.model.entity.User;
import com.example.model.vo.ResponseVO;
import com.example.model.vo.UserVO;
import com.example.service.UserService;
import com.example.util.JWTUtil;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;
    String registercdk= Contants.REGISTER_CDK;
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    //登录账号
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
            return new ResponseVO<>(401, "注册码错误,请联系管理员获取注册码", null);
        }
        // 角色集合
        Set<String> validRoles = new HashSet<>(Arrays.asList("chef", "waiter", "admin"));

        if (userDTO.getUsername().length() >= 6 && userDTO.getPassword().length() >= 6 && validRoles.contains(userDTO.getRole())) {
            if (userDAO.findByUsername(userDTO.getUsername())!=null){
                return new ResponseVO<>(401, "用户名已被注册", null);
            }
            User user = new User(userDTO.getUsername(),userDTO.getPassword(),userDTO.getRole());
            boolean Execution_result=userDAO.saveUser(user);
            if (Execution_result){
                return new ResponseVO<>(200, "登录成功", null);
            }
            return new ResponseVO<>(401, "未知错误", null);
        }
            return new ResponseVO<>(401, "账号密码格式不对", null);
    }
    //退出账号-删除token
    @Override
    public ResponseVO<Void> logouttoken(String token) {
        if(userDAO.delecttoken(token)>0){
            return new ResponseVO<>(200, "删除成功", null);
        }
        return new ResponseVO<>(401, "删除失败", null);
    }

}