package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.po.Customer;
import com.holy.mapper.CustomerMapper;
import com.holy.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerMapper customerMapper;
    @Override
    public IPage<Customer> list(Integer pageNum, Integer pageSize, String customerName) {
        // 创建IPage对象
        IPage<Customer> page = new Page<>(pageNum, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Customer> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断是否根据用户名查询
        if (customerName != null) {
            lambdaQueryWrapper.like(Customer::getCustomerName, customerName);
        }
        return customerMapper.selectPage(page, lambdaQueryWrapper);
    }
}
