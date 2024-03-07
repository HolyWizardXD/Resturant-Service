package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Tag(name = "菜品表单")
@Data
public class DishDTO {

    @NotNull(message = "菜品id不能为空,新增时随机填写id")
    @Schema(description = "id",type = "Integer", required = true)
    private Integer id;

    @NotBlank(message = "菜品名不能为空")
    @Schema(description = "菜品名",type = "String", required = true)
    private String dishName;

    @NotNull(message = "价格不能为空")
    @Min(0)
    @Schema(description = "价格", type = "float", required = true)
    private float price;

    @NotBlank(message = "分类不能为空")
    @Schema(description = "分类", type = "String", required = true)
    private String classify;

    @NotNull(message = "库存数量不能为空")
    @Schema(description = "库存", type = "Integer", required = true)
    private Integer stock;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", type = "Integer", required = true)
    private Integer status;
}
