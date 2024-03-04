package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 手机号
    private String phone;

    // 使用状态 1 正常 2 冻结
    private int status;

}
