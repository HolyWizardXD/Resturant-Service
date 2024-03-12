package com.holy.domain.vo;

import lombok.Data;

@Data
public class CustomerLoginVO {

    private String token;

    private int id;

    private String customerName;

}
