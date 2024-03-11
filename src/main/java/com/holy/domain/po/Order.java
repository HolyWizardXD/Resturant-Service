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
    private Integer id;

    // 餐桌号
    private Integer table;

    // 顾客id
    private Integer customerId;

    // 顾客名
    private String customerName;

    // 总价格
    private float allPrice;

    // 创建时间
    private LocalDateTime createTime;

    // 备注
    private String description;

}
