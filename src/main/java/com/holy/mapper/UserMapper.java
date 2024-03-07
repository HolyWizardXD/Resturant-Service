package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Update("update user set password=#{password} where id=#{id}")
    int updatePasswordById(int id,String password);

    @Update("update user set username=#{username},phone=#{phone} where id=#{id}")
    int updateUserInfoById(int id, String username, String phone);
}
