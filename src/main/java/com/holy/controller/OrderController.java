package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.OrderDTO;
import com.holy.domain.po.Result;
import com.holy.domain.vo.CustomerCountVO;
import com.holy.domain.vo.OrderVO;
import com.holy.service.CustomerService;
import com.holy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "分页查询订单接口")
    public Result<IPage<OrderVO>> list(
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String customerName,
            @RequestParam(required = false) LocalDateTime begin,
            @RequestParam(required = false) LocalDateTime end){
        // 调用订单接口 返回OrderVO列表
        IPage<OrderVO> iPage = orderService.list(pageNum, pageSize, customerName, begin, end);
        return Result.success(iPage);
    }

    @GetMapping("/selectByCustomerId")
    @Operation(summary = "根据顾客id查询订单接口")
    public Result<List<OrderVO>> selectById(@RequestParam(required = true) Integer customerId) {
        if(customerService.selectById(customerId) == null) return Result.error("该顾客不存在");
        return Result.success(orderService.selectByCustomerId(customerId));
    }

    @GetMapping("/selectByOrderId")
    @Operation(summary = "根据订单id查询订单")
    public Result<OrderVO> selectByOrderId(@RequestParam(required = true) Integer orderId) {
        // 调用订单接口 返回Order对象
        OrderVO orderVO = orderService.selectById(orderId);
        if(orderVO == null) return Result.error("该订单不存在");
        return Result.success(orderVO);
    }

    @GetMapping("/count")
    @Operation(summary = "根据订单id查询订单数量以及金额")
    public Result<CustomerCountVO> count(@RequestParam(required = true) Integer customerId) {

        // 调用订单接口 返回Order对象
        CustomerCountVO countVO = new CustomerCountVO();
        // 查看顾客是否消费过
        if(orderService.selectByCustomerId(customerId).isEmpty()) {
            countVO.setCustomerOrderCount(0);
            countVO.setCustomerTotalPrice(0);
            return Result.error("顾客未进行过消费");
        }
        countVO.setCustomerOrderCount(orderService.selectCountByCustomerId(customerId));
        countVO.setCustomerTotalPrice(orderService.selectPriceByCustomerId(customerId));
        return Result.success(countVO);
    }

    @PostMapping("/createOrder")
    @Operation(summary = "创建订单接口")
    public Result createOrder(@RequestBody @Valid OrderDTO orderDTO) {
        // 传递给orderService 判断结果
        if(!orderService.createOrder(orderDTO)) {
            return Result.error("库存不足");
        }
        return Result.success();
    }

    @PutMapping("/status")
    @Operation(summary = "改变出餐状态接口")
    public Result status(@RequestParam Integer orderId){
        // 判断订单是否存在
        if(orderService.selectById(orderId) == null) return Result.error("该订单不存在");
        orderService.updateOrderStatus(orderId);
        return Result.success(null ,"出餐成功");
    }
}
