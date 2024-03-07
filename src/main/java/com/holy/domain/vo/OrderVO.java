package com.holy.domain.vo;

import com.holy.domain.po.OrderDish;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {

    // 顾客名
    private String customerName;

    // 总价格
    private float totalPrice;

    // 备注
    private String description;

    // 订单创建时间
    private LocalDateTime createTime;

    // OrderDish对象
    private OrderDish orderDish;
}
