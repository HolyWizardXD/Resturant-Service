package com.holy.domain.vo;

import lombok.Data;

@Data
public class UserLoginVO {

    private String token;

    private Long id;

    private String username;

}
