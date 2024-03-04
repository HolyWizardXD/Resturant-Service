package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("order")
public class Order {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 顾客id
    private long customerId;

    // 总价格
    private float totalPrice;

    // 创建时间
    private LocalDateTime stock;

}
