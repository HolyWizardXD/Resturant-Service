package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Tag(name = "订单表单")
@Data
public class OrderDTO {

    @NotNull
    @Schema(description = "桌号", type = "Integer", required = true)
    private Integer table;

    @NotNull(message = "顾客id不能为空")
    @Schema(description = "顾客id", type = "Integer", required = true)
    private Integer customerId;

    @NotBlank(message = "顾客名不能为空")
    @Schema(description = "顾客名", type = "String", required = true)
    private String customerName;

    @Schema(description = "备注", type = "String")
    private String description;

    @NotNull(message = "菜品列表不能为空")
    @Schema(description = "菜品列表", type = "List<OrderDishDTO>", required = true)
    private List<OrderDishDTO> orderDishDTOList;

}
