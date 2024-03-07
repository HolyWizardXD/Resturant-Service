package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.EmployeeDTO;
import com.holy.domain.po.Employee;
import com.holy.domain.po.Result;
import com.holy.service.EmployeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Tag(name = "员工相关接口")
@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    @Operation(summary = "分页查询员工列表接口")
    public Result<IPage<Employee>> list(
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String employeeName) {
        // 调用员工的分页查询
        IPage<Employee> iPage = employeeService.list(pageNum, pageSize, employeeName);
        return Result.success(iPage);
    }

    @PostMapping("/addEmployee")
    @Operation(summary = "新增员工接口")
    public Result addEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        // 封装Inventory 不设置id id自增
        Employee employee = new Employee();
        employee.setEmployeeName(employeeDTO.getEmployeeName())
                .setSalary(employeeDTO.getSalary())
                .setPhone(employeeDTO.getPhone())
                .setStatus(employeeDTO.getStatus());
        // 调用员工服务的新增
        employeeService.addEmployee(employee);
        return Result.success();
    }

    @PutMapping("/updateEmployee")
    @Operation(summary = "修改员工信息接口")
    public Result updateEmployee(@RequestBody @Valid EmployeeDTO employeeDTO) {
        // 根据id返回员工
        Employee employee = employeeService.selectEmployeeById(employeeDTO.getId());
        // 查询员工是否存在
        if(employee == null) return Result.error("员工不存在");
        // 封装回employee
        employee.setEmployeeName(employeeDTO.getEmployeeName())
                .setSalary(employeeDTO.getSalary())
                .setPhone(employeeDTO.getPhone())
                .setStatus(employeeDTO.getStatus());
        employeeService.updateEmployee(employee);
        return null;
    }

    @DeleteMapping("deleteEmployee")
    @Operation(summary = "删除员工")
    public Result deleteEmployee(@RequestParam Integer employeeId) {
        // 判断该员工是否存在
        if (employeeService.selectEmployeeById(employeeId) == null) return Result.error("该员工不存在");
        // 删除员工
        employeeService.deleteEmployeeById(employeeId);
        return Result.success();
    }
}
