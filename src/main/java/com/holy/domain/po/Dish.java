package com.holy.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("dish")
public class Dish implements Serializable{

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    // 菜品名称
    private String dishName;

    // 价格
    private float price;

    // 库存
    private Integer stock;

    // 分类
    private String classify;

    // 状态 1 启用 2 冻结
    private Integer status;

    // 照片
    private String pictureUrl;

}
