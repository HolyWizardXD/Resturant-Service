package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Order;
import com.holy.domain.po.Result;
import com.holy.domain.vo.OrderVO;
import com.holy.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "订单相关接口")
@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    @Operation(summary = "分页查询订单")
    public Result<IPage<OrderVO>> list(
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) LocalDateTime begin,
            @RequestParam(required = false) LocalDateTime end){
        // 调用订单接口 返回OrderVO列表
        IPage<OrderVO> iPage = orderService.list(pageNum, pageSize, begin, end);
        return Result.success(iPage);
    }

    @GetMapping("/selectById")
    @Operation(summary = "根据id查询")
    public Result<List<OrderVO>> selectById(@RequestParam(required = true) Integer customerId) {
        return Result.success(orderService.selectById(customerId));
    }
}
