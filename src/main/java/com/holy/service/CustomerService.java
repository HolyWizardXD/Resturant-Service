package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Customer;

public interface CustomerService {
    IPage<Customer> list(Integer pageNum, Integer pageSize, String customerName);

    Customer selectByName(String customerName);

    Customer selectById(int id);

    boolean register(Customer customer);

    boolean updatePasswordById(int id, String newPassword);

    boolean updateCustomerById(int id, String customerName, String phone);
}
