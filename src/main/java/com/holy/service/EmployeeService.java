package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Employee;

public interface EmployeeService {
    IPage<Employee> list(Integer pageNum, Integer pageSize, String employeeName);

    boolean addEmployee(Employee employee);

    boolean updateEmployee(Employee employee);

    Employee selectEmployeeById(Integer id);

    boolean deleteEmployeeById(Integer id);

    boolean updateOrderStatus(Integer id, Integer status);
}
