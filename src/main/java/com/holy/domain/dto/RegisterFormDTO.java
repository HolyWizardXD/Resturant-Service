package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Tag(name = "注册表单")
@Data
public class RegisterFormDTO {

    @NotBlank(message = "注册用户名不能为空")
    @Schema(description = "用户名",type = "String", required = true)
    @Length(message = "用户名长度区间{min}-{max}",min = 6, max = 16)
    private String username;

    @NotBlank(message = "注册密码不能为空")
    @Schema(description = "密码", type = "String", required = true)
    @Length(message = "密码长度区间{min}-{max}",min = 8, max = 20)
    private String password;

    @NotBlank(message = "重复密码不能为空")
    @Schema(description = "重复新密码", type = "String", required =true)
    @Length(message = "密码长度区间{min}-{max}",min = 8, max = 20)
    private String rePassword;

    @NotBlank(message = "注册手机号码不能为空")
    @Schema(description = "手机号", type = "String", required =true)
    @Pattern(regexp = "(?:(?:\\+|00)86)?1\\d{10}", message = "手机号格式错误")
    private String phone;
}
