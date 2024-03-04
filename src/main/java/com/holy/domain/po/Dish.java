package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("dish")
public class Dish {

    @TableId(value = "id", type = IdType.AUTO)
    private long id;

    // 菜品名称
    private String dishName;

    // 价格
    private float price;

    // 库存
    private String stock;

    // 状态 1 启用 2 冻结
    private int status;

}
