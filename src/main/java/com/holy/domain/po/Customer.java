package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("customer")
public class Customer implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    // 顾客名称
    private String customerName;

    // 密码
    private String password;

    // 手机号
    private String phone;

    // 创建时间
    private LocalDateTime createTime;

}
