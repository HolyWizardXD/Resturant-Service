package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Tag(name = "顾客手机号登录表单")
@Data
public class LoginPhoneFormDTO {

    @NotBlank(message = "手机号码不能为空")
    @Schema(description = "手机号", type = "String", required =true)
    @Pattern(regexp = "(?:(?:\\+|00)86)?1\\d{10}", message = "手机号格式错误")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "密码", type = "String", required = true)
    private String password;

}
