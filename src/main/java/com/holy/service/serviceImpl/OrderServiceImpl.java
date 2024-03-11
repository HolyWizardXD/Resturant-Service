package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.dto.OrderDTO;
import com.holy.domain.dto.OrderDishDTO;
import com.holy.domain.po.Order;
import com.holy.domain.po.OrderDish;
import com.holy.domain.vo.OrderVO;
import com.holy.mapper.OrderDishMapper;
import com.holy.mapper.OrderMapper;
import com.holy.service.DishService;
import com.holy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDishMapper orderDishMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private DishService dishService;

    @Override
    public IPage<OrderVO> list(Integer pageNum, Integer pageSize, String customerName, LocalDateTime begin, LocalDateTime end) {
        // 创建IPage对象
        IPage<OrderVO> iPage = new Page<>(pageNum, pageSize);
        // 把iPage传入Mapper 自动拦截分页
        iPage = orderMapper.list(iPage, customerName, begin, end);
        return iPage;
    }

    @Override
    public List<OrderVO> selectByCustomerId(Integer customerId) {
        // 查询该顾客所有订单
        return orderMapper.listAll(customerId);
    }

    @Override
    public OrderVO selectById(Integer orderId) {
        return orderMapper.selectByOrderId(orderId);
    }

    // 事务注释
    @Override
    @Transactional
    public synchronized boolean createOrder(OrderDTO orderDTO) {
        // 取出orderDTO中的数据
        Integer table = orderDTO.getTable();
        Integer customerId = orderDTO.getCustomerId();
        String customerName = orderDTO.getCustomerName();
        String description = orderDTO.getDescription();
        // 取出orderDTO中的List<OrderDishDTO>
        List<OrderDishDTO> orderDishDTOList = orderDTO.getOrderDishDTOList();
        // 判断库存是否足够
        for (OrderDishDTO orderDishDTO : orderDishDTOList) {
            if (orderDishDTO.getAmount() > dishService.selectDishStockById(orderDishDTO.getDishId())) {
                return false;
            }
        }
        // 计算总价
        float allPrice = 0;
        for (OrderDishDTO orderDishDTO : orderDishDTOList) {
            allPrice += orderDishDTO.getAmount() * dishService.selectDishPriceById(orderDishDTO.getDishId());
        }
        // 封装Order
        Order order = new Order();
        order.setTable(table)
                .setCustomerId(customerId)
                .setCustomerName(customerName)
                .setDescription(description)
                .setAllPrice(allPrice);
        // 插入order表
        orderMapper.insertOrder(order);
        int orderId = order.getId();
        // 遍历插入order_dish表
        for (OrderDishDTO orderDishDTO : orderDishDTOList) {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderId(orderId)
                    .setDishId(orderDishDTO.getDishId())
                    .setAmount(orderDishDTO.getAmount())
                    .setTotalPrice(orderDishDTO.getAmount() * dishService.selectDishPriceById(orderDishDTO.getDishId()));
            orderDishMapper.insert(orderDish);
        }
        return true;
    }

    // 手写分页查询 (废弃)
    /*@Override
    public IPage<OrderVO> selectLimit(Integer pageNum, Integer pageSize, LocalDateTime begin, LocalDateTime end) {
        // 创建IPage对象
        IPage<OrderVO> iPage = new Page<>();
        // 查询总数
        int total = orderMapper.countOrder();
        // 计算页数
        int pages = 0;
        if(total < pageSize) {
            pages = 1;
        }else if(total % pageSize == 0) {
            pages = total / pageSize;
        }else if(total % pageSize != 0) {
            pages = (total / pageSize) + 1;
        }
        // 封装Page对象
        iPage.setRecords(orderMapper.selectLimit(pageSize, pageNum-1, begin, end))
                .setTotal(total)
                .setSize(pageSize)
                .setCurrent(pageNum)
                .setPages(pages);
        return iPage;
    }*/
}
