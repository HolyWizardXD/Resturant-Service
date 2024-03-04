package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("customer")
public class Customer {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 顾客名称
    private String customerName;

    // 密码
    private String password;

    // 手机号
    private String phone;

    // 创建时间
    private LocalDateTime createTime;

}
