package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
