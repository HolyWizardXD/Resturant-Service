package com.holy.service;

import com.holy.domain.po.User;

public interface UserService {

    User selectUserByName(String username);

    boolean register(User user);

    User selectUserById(int id);

    boolean updatePasswordById(int id, String newPassword);

    boolean updateUserInfoById(int id, String username, String phone);

    boolean logoff(int id);

    String selectPhoneById(int id);
}
