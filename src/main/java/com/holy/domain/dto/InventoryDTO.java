package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Tag(name = "修改新增库存表单")
@Data
public class InventoryDTO {

    @NotNull(message = "原料id不能为空,新增时随机填写id")
    @Schema(description = "id",type = "Integer", required = true)
    private Integer id;

    @NotBlank(message = "原料名不能为空")
    @Schema(description = "原料名",type = "String", required = true)
    private String material;

    @NotNull(message = "库存不能为空")
    @Min(0)
    @Schema(description = "库存", type = "float", required = true)
    private float stock;

}
