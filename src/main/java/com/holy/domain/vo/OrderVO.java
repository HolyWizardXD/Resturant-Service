package com.holy.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderVO {

    // 订单id
    private Integer id;

    // 餐桌号
    private Integer table;

    // 顾客名
    private String customerName;

    // 总价格
    private float allPrice;

    // 备注
    private String description;

    // 订单创建时间
    private LocalDateTime createTime;

    // DishVO List
    private List<DishVO> dishVOList;
}
