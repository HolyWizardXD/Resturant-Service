package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.*;
import com.holy.domain.po.Customer;
import com.holy.domain.po.Result;
import com.holy.domain.vo.CustomerLoginVO;
import com.holy.domain.vo.CustomerVO;
import com.holy.service.CustomerService;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Tag(name = "顾客相关接口")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/list")
    @Operation(summary = "分页查询顾客接口")
    public Result<IPage<CustomerVO>> list(
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String customerName) {
        // 调用顾客服务的分页查询
        IPage<CustomerVO> page = customerService.list(pageNum, pageSize, customerName);
        return Result.success(page);
    }

    @Operation(summary = "顾客登录接口")
    @PostMapping("/login")
    public Result<CustomerLoginVO> login(@RequestBody @Valid LoginFormDTO loginFormDTO) { // 复用用户登录表单
        // 获取loginFormDTO用户登陆表单中的数据
        String customerName = loginFormDTO.getUsername();
        String password = loginFormDTO.getPassword();
        // 根据顾客名查询顾客
        Customer customer = customerService.selectByName(customerName);
        // 顾客是否存在
        if(customer == null) return Result.error("顾客不存在");
        // 校验密码
        if(!Md5Util.getMD5String(password).equals(customer.getPassword())) return Result.error("密码错误");
        // 生成Token
        Map<String, Object> claims = new HashMap<>();
        // Token放入用户id和用户名
        claims.put("id", customer.getId());
        claims.put("customerName", customer.getCustomerName());
        String token = JwtUtil.getToken(claims, true);
        // Token存入Redis
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        // 设置30天过期时间 与Token相同
        operations.set(token, token, 30, TimeUnit.DAYS);
        // 封装CustomerLoginVO返回
        CustomerLoginVO customerLoginVO = new CustomerLoginVO();
        customerLoginVO.setToken(token);
        customerLoginVO.setId(customer.getId());
        customerLoginVO.setCustomerName(customer.getCustomerName());
        return Result.success(customerLoginVO);
    }

    @Operation(summary = "顾客注册接口")
    @PostMapping("/register")
    public Result register(@RequestBody @Valid RegisterFormDTO registerFormDTO) {
        // 获取registerFormDTO用户注册表单中的数据
        String customerName = registerFormDTO.getUsername();
        String password = registerFormDTO.getPassword();
        String rePassword = registerFormDTO.getRePassword();
        String phone = registerFormDTO.getPhone();
        // 判断密码填写是否一致
        if (!password.equals(rePassword)) return Result.error("两次填写密码不一致");
        // 查询顾客是否已经存在
        if (customerService.selectByName(customerName) != null) return Result.error("顾客名已存在");
        // 加密密码
        password = Md5Util.getMD5String(password);
        // 封装顾客对象
        Customer customer = new Customer()
                .setCustomerName(customerName)
                .setPassword(password)
                .setPhone(phone)
                .setCreateTime(LocalDateTime.now());
        // 注册顾客
        if (customerService.register(customer)) {
            return Result.success(null, "注册成功");
        }else {
            return Result.error("注册失败");
        }
    }

    @Operation(summary = "修改顾客密码接口")
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
        Customer customer = customerService.selectById(id);
        // 原密码填写是否正确
        if (!customer.getPassword().equals(Md5Util.getMD5String(oldPassword))) return Result.error("原密码填写错误");
        // 判断密码填写是否一致
        if (!rePassword.equals(newPassword)) return Result.error("新密码两次填写不一致");
        // 加密密码
        newPassword = Md5Util.getMD5String(newPassword);
        // 更新密码
        if (customerService.updatePasswordById(id, newPassword)) {
            // 删除Redis中的Token
            stringRedisTemplate.opsForValue().getOperations().delete(token);
            return Result.success(null, "密码修改成功");
        }else {
            return Result.error("密码修改失败");
        }
    }

    @Operation(summary = "修改顾客信息接口")
    @PutMapping("/updateUserInfo")
    public Result updateUserInfo(@RequestBody @Valid UpdateCustomerFormDTO updateCustomerFormDTO,
                                 @RequestHeader("Authorization") String token) {
        // 从updateUserFormDTO表单中取出用户信息
        String customerName = updateCustomerFormDTO.getCustomerName();
        String phone = updateCustomerFormDTO.getPhone();
        // 从ThreadLocal中取出用户id
        Map<String, Object> claims = ThreadLocalUtil.get();
        int id = (Integer) claims.get("id");
        // 判断用户名是否重复
        if(customerService.selectByName(customerName) != null) return Result.error("该顾客名已被占用");
        // 判断修改内容
        Customer customer = customerService.selectById(id);
        if(customerName == null && phone == null) return Result.error("无参数");
        // 顾客名无需修改
        if(customerName == null) customerName = customer.getCustomerName();
        // 手机号无需修改
        if(phone == null) phone = customer.getPhone();
        // 修改顾客信息
        if(customerService.updateCustomerById(id, customerName, phone)) {
            // 删除Redis中的Token
            ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
            operations.getOperations().delete(token);
            return Result.success(null, "顾客信息修改成功");
        }else {
            return Result.error("顾客信息修改失败");
        }
    }

}
