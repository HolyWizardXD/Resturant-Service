package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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
        return new LambdaQueryChainWrapper<>(userMapper).eq(User::getUsername,username).one();
    }
}
