package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("order")
public class Order implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 顾客id
    private long customerId;

    // 总价格
    private float totalPrice;

    // 创建时间
    private LocalDateTime stock;

    // 备注
    private String description;

}
