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

    @NotNull(message = "菜品id不能为空")
    @Schema(description = "id",type = "int", required = true)
    private int id;

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
    @Schema(description = "库存", type = "int", required = true)
    private int stock;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", type = "int", required = true)
    private int status;

    @NotBlank(message = "图片不能为空")
    @Schema(description = "图片", type = "String", required = true)
    private String pictureUrl;
}
