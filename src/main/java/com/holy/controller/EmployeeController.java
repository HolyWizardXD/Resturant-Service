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

import java.awt.geom.Arc2D;

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
                .setPhone(employeeDTO.getPhone());
        // 调用员工服务的新增
        employeeService.addEmployee(employee);
        return Result.success(null,"新增成功");
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
                .setPhone(employeeDTO.getPhone());
        employeeService.updateEmployee(employee);
        return Result.success(null,"员工修改成功");
    }

    @DeleteMapping("/deleteEmployee")
    @Operation(summary = "删除员工")
    public Result deleteEmployee(@RequestParam Integer employeeId) {
        // 判断该员工是否存在
        if (employeeService.selectEmployeeById(employeeId) == null) return Result.error("该员工不存在");
        // 删除员工
        employeeService.deleteEmployeeById(employeeId);
        return Result.success();
    }

    @PutMapping("/status")
    @Operation(summary = "改变员工状态接口")
    public Result status(@RequestParam Integer id,@RequestParam Integer status){
        // 判断员工是否存在
        if(employeeService.selectEmployeeById(id) == null) return Result.error("该员工不存在");
        // 判断status是否合法
        if(status != 1 && status != 2) return Result.error("状态值不合法");
        employeeService.updateOrderStatus(id,status);
        return Result.success();
    }
}
