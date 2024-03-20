package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
    @Update("update employee set status=#{status} where id=#{id}")
    boolean updateStatusById(Integer id, Integer status);
}
