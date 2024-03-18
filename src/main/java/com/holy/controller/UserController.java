package com.holy.controller;

import cn.hutool.core.util.RandomUtil;
import com.holy.domain.dto.LoginFormDTO;
import com.holy.domain.dto.RegisterFormDTO;
import com.holy.domain.dto.UpdatePasswordFromDTO;
import com.holy.domain.dto.UpdateUserFormDTO;
import com.holy.domain.po.Result;
import com.holy.domain.po.User;
import com.holy.domain.vo.UserLoginVO;
import com.holy.service.UserService;
import com.holy.utils.JwtUtil;
import com.holy.utils.Md5Util;
import com.holy.utils.ThreadLocalUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static com.holy.common.CommonField.FROZEN;

@Tag(name = "用户相关接口")
@RestController
@Validated
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Operation(summary = "用户登录接口")
    @PostMapping("/login")
    public Result<UserLoginVO> login(@RequestBody @Valid LoginFormDTO loginFormDTO,
                                     @RequestHeader(value = "Authorization",required = false) String hasToken) {
        // 判断是否已经登陆
        if(hasToken == null) hasToken = "token" + RandomUtil.randomString(8);
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(hasToken))) return Result.error("请勿重复登录");
        // 获取loginFormDTO用户登陆表单中的数据
        String username = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 根据用户名查询用户
        User user = userService.selectUserByName(username);
        // 用户是否存在
        if(user == null) return Result.error("用户不存在");
        // 校验用户的状态
        if(Objects.equals(user.getStatus(), FROZEN)) return Result.error("用户被冻结");
        // 校验密码
        if(!Md5Util.getMD5String(password).equals(user.getPassword())) return Result.error("密码错误");
        // 生成Token
        Map<String, Object> claims = new HashMap<>();
        // Token放入用户id和用户名
        claims.put("id", user.getId());
        claims.put("username", user.getUsername());
        String token = JwtUtil.getToken(claims);
        // Token存入Redis
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        // 设置1天过期时间 与Token相同
        operations.set(token, token, 1, TimeUnit.DAYS);
        // 封装UserLoginVO返回
        UserLoginVO userLoginVO = new UserLoginVO();
        userLoginVO.setToken(token);
        userLoginVO.setId(user.getId());
        userLoginVO.setUsername(user.getUsername());
        return Result.success(userLoginVO, "登陆成功");
    }

    @Operation(summary = "用户登出接口")
    @DeleteMapping("/logout")
    public Result logout(@RequestHeader(value = "Authorization",required = true) String token) {
        stringRedisTemplate.delete(token);
        return Result.success(null, "登出成功");
    }

    @Operation(summary = "查询用户电话接口")
    @GetMapping("/getPhone")
    public Result<String> getPhone() {
        Map<String, Object> claims = ThreadLocalUtil.get();
        int id = (Integer) claims.get("id");
        return Result.success(userService.selectPhoneById(id));
    }

    @Operation(summary = "用户注销接口")
    @DeleteMapping("/logoff")
    public Result logoff(@RequestParam Integer userId,@RequestHeader(value = "Authorization",required = true) String token) {
        // 从ThreadLocal中取出token中解析出用户id
        Map<String, Object> claims = ThreadLocalUtil.get();
        int id = (Integer) claims.get("id");
        if(id != userId) return Result.error("注销失败,解析错误");
        userService.logoff(id);
        stringRedisTemplate.delete(token);
        return Result.success(null, "登出成功");
    }

    @Operation(summary = "用户注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterFormDTO registerFormDTO){
        // 获取registerFormDTO用户注册表单中的数据
        String username = registerFormDTO.getUsername();
        String password = registerFormDTO.getPassword();
        String rePassword = registerFormDTO.getRePassword();
        String phone = registerFormDTO.getPhone();
        // 判断密码填写是否一致
        if (!password.equals(rePassword)) return Result.error("两次填写密码不一致");
        // 查询用户是否已经存在
        User user = userService.selectUserByName(username);
        if (user != null) return Result.error("用户名已存在");
        // 加密密码
        password = Md5Util.getMD5String(password);
        // 封装User对象
        User registerUser = new User()
                .setStatus(1)
                .setUsername(username)
                .setPassword(password)
                .setPhone(phone);
        // 注册用户
        if (userService.register(registerUser)) {
            return Result.success(null, "注册成功");
        }else {
            return Result.error("注册失败");
        }
    }

    @Operation(summary = "修改密码接口")
    @PatchMapping("/updatePassword")
    public Result updatePassword (@RequestBody @Valid UpdatePasswordFromDTO updatePasswordFromDto,
                                  @RequestHeader("Authorization") String token){
        // 获取updatePasswordFromDto用户修改密码表单中的数据
        String oldPassword = updatePasswordFromDto.getOldPassword();
        String newPassword = updatePasswordFromDto.getNewPassword();
        String rePassword = updatePasswordFromDto.getRePassword();
        // 从ThreadLocal中取出用户信息
        Map<String, Object> claims = ThreadLocalUtil.get();
        // 根据用户id拿到原密码对比
        int id = (Integer) claims.get("id");
        User user = userService.selectUserById(id);
        // 原密码填写是否正确
        if (!Md5Util.getMD5String(oldPassword).equals(user.getPassword())) return Result.error("原密码填写错误");
        // 判断密码填写是否一致
        if (!newPassword.equals(rePassword)) return Result.error("两次填写密码不一致");
        // 加密密码
        newPassword = Md5Util.getMD5String(newPassword);
        // 更新密码
        if (userService.updatePasswordById(id, newPassword)) {
            // 删除Redis中的Token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.getOperations().delete(token);
            return Result.success(null,"密码修改成功,请重新登录");
        }else {
            return Result.error("密码修改失败");
        }
    }

    @Operation(summary = "修改用户信息接口")
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody @Valid UpdateUserFormDTO updateUserFormDTO,
                                 @RequestHeader("Authorization") String token) {
        // 从updateUserFormDTO表单中取出用户信息
        String username = updateUserFormDTO.getUsername();
        String phone = updateUserFormDTO.getPhone();
        // 从ThreadLocal中取出用户id
        Map<String, Object> claims = ThreadLocalUtil.get();
        int id = (Integer) claims.get("id");
        // 判断用户名是否重复
        if(userService.selectUserByName(username) != null) return Result.error("该用户名已被占用");
        // 判断修改内容
        User user = userService.selectUserById(id);
        if(username == null && phone == null) return Result.error("无参数");
        // 用户名无需修改
        if(username == null) username = user.getUsername();
        // 手机号无需修改
        if(phone == null) phone = user.getPhone();
        // 修改用户信息
        if(userService.updateUserInfoById(id, username, phone)) {
            // 删除Redis中的Token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.getOperations().delete(token);
            return Result.success(null, "用户信息修改成功");
        }else {
            return Result.error("用户信息修改失败");
        }
    }
}
