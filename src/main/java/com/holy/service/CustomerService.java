package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Customer;
import com.holy.domain.vo.CustomerVO;

public interface CustomerService {
    IPage<CustomerVO> list(Integer pageNum, Integer pageSize, String customerName);

    Customer selectByName(String customerName);

    Customer selectById(int id);

    boolean register(Customer customer);

    boolean updatePasswordById(int id, String newPassword);

    boolean updateCustomerById(int id, String customerName, String phone);
}
