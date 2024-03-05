package com.holy.service;

import com.holy.domain.po.User;

public interface UserService {

    User selectUserByName(String username);

}
