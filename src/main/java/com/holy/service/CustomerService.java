package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Customer;

public interface CustomerService {
    IPage<Customer> list(Integer pageNum, Integer pageSize, String customerName);
}
