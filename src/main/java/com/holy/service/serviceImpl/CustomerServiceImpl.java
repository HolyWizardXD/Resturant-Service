package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
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

    @Override
    public Customer selectByName(String customerName) {
        // 调用链式条件构造器 根据顾客名返回单个顾客
        return new LambdaQueryChainWrapper<>(Customer.class).eq(Customer::getCustomerName, customerName).one();
    }

    @Override
    public Customer selectById(int id) {
        return new LambdaQueryChainWrapper<>(Customer.class).eq(Customer::getId, id).one();
    }

    @Override
    public boolean register(Customer customer) {
        return customerMapper.insert(customer) > 0;
    }

    @Override
    public boolean updatePasswordById(int id, String newPassword) {
        return customerMapper.updatePasswordById(id, newPassword) > 0;
    }

    @Override
    public boolean updateCustomerById(int id, String customerName, String phone) {
        return customerMapper.updateCustomerById(id, customerName, phone) > 0;
    }

}
