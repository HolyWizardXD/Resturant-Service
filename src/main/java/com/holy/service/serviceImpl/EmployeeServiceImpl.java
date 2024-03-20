package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.po.Employee;
import com.holy.domain.po.Inventory;
import com.holy.mapper.EmployeeMapper;
import com.holy.mapper.InventoryMapper;
import com.holy.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    @Override
    public IPage<Employee> list(Integer pageNum, Integer pageSize, String employeeName) {
        // 创建IPage对象
        IPage<Employee> page = new Page<>(pageNum, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断条件是否为空
        if (employeeName != null) {
            // 员工名
            lambdaQueryWrapper.like(Employee::getEmployeeName, employeeName);
        }
        return employeeMapper.selectPage(page, lambdaQueryWrapper);
    }

    @Override
    public boolean addEmployee(Employee employee) {
        return employeeMapper.insert(employee) > 0;
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        return employeeMapper.updateById(employee) > 0;
    }

    @Override
    public Employee selectEmployeeById(Integer id) {
        return employeeMapper.selectById(id);
    }

    @Override
    public boolean deleteEmployeeById(Integer id) {
        return employeeMapper.deleteById(id) > 0;
    }


    @Override
    public boolean updateOrderStatus(Integer id, Integer status) {
        return employeeMapper.updateStatusById(id,status);
    }
}
