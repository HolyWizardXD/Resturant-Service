package com.holy.controller;

import com.holy.domain.dto.LoginFormDTO;
import com.holy.domain.po.Result;
import com.holy.domain.po.User;
import com.holy.domain.vo.UserLoginVO;
import com.holy.status.UserStatus;
import com.holy.service.UserService;
import com.holy.utils.JwtUtil;
import com.holy.utils.Md5Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Validated LoginFormDTO loginFormDTO){

        // 获取loginFormDTO中的数据
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
}
