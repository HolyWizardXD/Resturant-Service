package com.holy.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CustomerLoginVO {

    private String token;

    private int id;

    private String username;

    private String phone;

    private LocalDateTime createTime;
}
