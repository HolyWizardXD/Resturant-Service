package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.holy.domain.po.Dish;
import com.holy.domain.po.User;
import com.holy.mapper.UserMapper;
import com.holy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User selectUserByName(String username) {
        // 调用链式条件构造器 根据用户名名返回单个用户
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getUsername, username).one();
    }

    @Override
    public boolean register(User user) {
        return userMapper.insert(user) > 0;
    }

    @Override
    public User selectUserById(int id) {
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getId, id).one();
    }

    @Override
    public boolean updatePasswordById(int id, String newPassword) {
        return userMapper.updatePasswordById(id, newPassword) > 0;
    }

    @Override
    public boolean updateUserInfoById(int id, String username, String phone) {
        return userMapper.updateUserInfoById(id, username, phone) > 0;
    }


}
