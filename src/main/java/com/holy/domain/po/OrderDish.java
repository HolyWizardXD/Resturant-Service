package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order_dish")
public class OrderDish {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 顾客id
    private Integer orderId;

    // 菜品id
    private Integer dishId;

    // 数量
    private Integer amount;

    // 价格
    private float price;

}


