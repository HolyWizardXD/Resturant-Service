package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("employee")
public class Employee {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 员工名
    private String employeeName;

    // 薪资
    private float salary;

    // 手机号
    private String phone;

    // 状态 1 启用 2 冻结
    private int status;

}

