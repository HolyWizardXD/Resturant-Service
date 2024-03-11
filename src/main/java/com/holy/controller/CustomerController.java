package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Customer;
import com.holy.domain.po.Result;
import com.holy.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "顾客相关接口")
@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/list")
    @Operation(summary = "分页查询顾客接口")
    public Result<IPage<Customer>> list(
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String customerName) {
        // 调用顾客服务的分页查询
        IPage<Customer> page = customerService.list(pageNum, pageSize, customerName);
        return Result.success(page);
    }

}
