package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.Customer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface CustomerMapper extends BaseMapper<Customer> {
    @Update("update customer set password=#{password} where id=#{id}")
    int updatePasswordById(int id, String password);

    @Update("update customer set customer_name=#{customerName},phone=#{phone} where id=#{id}")
    int updateCustomerById(int id, String customerName, String phone);
}
