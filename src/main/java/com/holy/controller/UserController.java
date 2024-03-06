package com.holy.controller;

import com.holy.domain.dto.LoginFormDTO;
import com.holy.domain.dto.RegisterFormDTO;
import com.holy.domain.po.Result;
import com.holy.domain.po.User;
import com.holy.domain.vo.UserLoginVO;
import com.holy.status.UserStatus;
import com.holy.service.UserService;
import com.holy.utils.JwtUtil;
import com.holy.utils.Md5Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "用户相关接口")
@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Valid LoginFormDTO loginFormDTO){
        // 获取loginFormDTO用户登陆表单中的数据
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 根据用户名查询用户
        User user = userService.selectUserByName(username);
        // 用户是否存在
        if(user == null) return Result.error("用户不存在");
        // 校验用户的状态
        if(user.getStatus() == UserStatus.FROZEN) return Result.error("用户被冻结");
        // 校验密码
        if(!Md5Util.getMD5String(password).equals(user.getPassword())) return Result.error("密码错误");
        // 生成Token
        Map<String, Object> claims = new HashMap<>();
        // Token放入用户id和用户名
        claims.put("id",user.getId());
        claims.put("username",user.getUsername());
        String token = JwtUtil.getToken(claims);
        // 封装UserLoginVO返回
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        return Result.success(userLoginVO);
    }

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterFormDTO registerFormDTO){
        // 获取registerFormDTO用户注册表单中的数据
        String username = registerFormDTO.getUsername();
        String password = registerFormDTO.getPassword();
        String phone = registerFormDTO.getPhone();
        // 查询用户是否已经存在
        User user = userService.selectUserByName(username);
        if (user == null) {return Result.error("该用户已经存在");}
        // 加密密码
        password = Md5Util.getMD5String(password);
        // 封装User对象
        User registerUser = new User()
                .setStatus(1)
                .setUsername(username)
                .setPassword(password)
                .setPhone(phone);
        // 注册用户
        if(userService.register(registerUser)) {
            return Result.success("注册成功");
        }else {
            return Result.error("注册失败");
        }
    }
}
