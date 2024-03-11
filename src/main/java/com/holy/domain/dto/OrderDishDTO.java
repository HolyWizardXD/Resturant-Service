package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Tag(name = "订单菜品表单")
@Data
public class OrderDishDTO {

    @NotNull(message = "菜品id不能为空")
    @Schema(description = "菜品id",type = "Integer", required = true)
    private Integer dishId;

    @NotNull(message = "菜品数量不能为空")
    @Min(1)
    @Schema(description = "菜品数量",type = "Integer", required = true)
    private Integer amount;
}
