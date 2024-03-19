package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.OrderDTO;
import com.holy.domain.vo.OrderVO;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    IPage<OrderVO> list(Integer pageNum, Integer pageSize, String customerName, LocalDateTime begin, LocalDateTime end);

    List<OrderVO> selectByCustomerId(Integer customerId);

    boolean createOrder(OrderDTO orderDTO);

    OrderVO selectById(Integer orderId);

    boolean updateOrderStatus(Integer orderId);

    // 手写分页查询(废弃)
    // IPage<OrderVO> selectLimit(Integer pageNum, Integer pageSize, LocalDateTime begin, LocalDateTime end);
}
