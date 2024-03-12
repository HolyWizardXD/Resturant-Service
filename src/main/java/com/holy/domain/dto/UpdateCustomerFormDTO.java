package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Tag(name = "修改顾客信息表单")
@Data
public class UpdateCustomerFormDTO {

    @Schema(description = "顾客名",type = "String", required = false)
    @Length(message = "顾客名长度区间{min}-{max}",min = 6, max = 16)
    private String customerName;

    @Schema(description = "手机号", type = "String", required = false)
    @Pattern(regexp = "(?:(?:\\+|00)86)?1\\d{10}", message = "手机号格式错误")
    private String phone;
}
