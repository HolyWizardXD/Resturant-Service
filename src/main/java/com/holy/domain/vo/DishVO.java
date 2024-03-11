package com.holy.domain.vo;

import lombok.Data;

@Data
public class DishVO {

    private Integer dish_id;

    private String dish_name;

    private float price;

    private Integer amount;

    private String totalPrice;
}
