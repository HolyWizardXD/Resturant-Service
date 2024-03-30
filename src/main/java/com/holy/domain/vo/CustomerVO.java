package com.holy.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class CustomerVO {
    // 顾客id
    private Integer id;

    // 顾客名称
    private String customerName;

    // 手机号
    private String phone;

    // 创建时间
    private LocalDateTime createTime;
}
