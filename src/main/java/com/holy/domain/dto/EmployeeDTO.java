package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Tag(name = "修改新增员工表单")
@Data
public class EmployeeDTO {

    @NotNull(message = "员工id不能为空,新增时随机填写id")
    @Schema(description = "id",type = "Integer", required = true)
    private Integer id;

    @NotBlank(message = "员工名不能为空")
    @Schema(description = "员工名",type = "String", required = true)
    private String employeeName;

    @NotNull(message = "薪资不能为空")
    @Min(0)
    @Schema(description = "薪资", type = "float", required = true)
    private float salary;

    @NotBlank(message = "手机号码不能为空")
    @Schema(description = "手机号", type = "String", required =true)
    @Pattern(regexp = "(?:(?:\\+|00)86)?1\\d{10}", message = "手机号格式错误")
    private String phone;

    @NotNull(message = "状态不能为空")
    @Schema(description = "状态", type = "Integer", required = true)
    private Integer status;
}
