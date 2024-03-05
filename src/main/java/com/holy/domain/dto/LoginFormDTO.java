package com.holy.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Tag(name = "用户登录表单")
@Data
public class LoginFormDTO {

    @Schema(description = "用户名",type = "String", required = true)
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    @Schema(description = "密码", type = "String", required = true)
    private String password;
}
