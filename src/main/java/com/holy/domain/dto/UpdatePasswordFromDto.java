package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Tag(name = "修改密码表单")
@Data
public class UpdatePasswordFromDto {

    @NotBlank(message = "旧密码不能为空")
    @Schema(description = "旧密码",type = "String", required = true)
    private String oldPassword;

    @NotBlank(message = "新密码不能为空")
    @Schema(description = "新密码", type = "String", required = true)
    @Length(message = "密码长度区间{min}-{max}",min = 8, max = 20)
    private String newPassword;

    @NotBlank(message = "重复密码不能为空")
    @Schema(description = "重复新密码", type = "String", required =true)
    @Length(message = "密码长度区间{min}-{max}",min = 8, max = 20)
    private String rePassword;

}
