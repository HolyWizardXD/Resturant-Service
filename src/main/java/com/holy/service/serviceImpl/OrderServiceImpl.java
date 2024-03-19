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
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderDishMapper orderDishMapper;

    @Resource
    private RabbitTemplate rabbitTemplate;

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

    @Override
    public boolean updateOrderStatus(Integer orderId) {
        return orderMapper.updateOrderStatus(orderId) > 0;
    }

    // 事务注释
    @Override
    @Transactional
    public synchronized boolean createOrder(OrderDTO orderDTO) {
        // 取出orderDTO中的数据
        Integer table = orderDTO.getTable();
        Integer customerId = orderDTO.getCustomerId();
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
                .setDescription(description)
                .setAllPrice(allPrice);
        // 插入order表
        orderMapper.insertOrder(order);
        int orderId = order.getId();
        // 遍历插入order_dish表 扣减库存
        for (OrderDishDTO orderDishDTO : orderDishDTOList) {
            OrderDish orderDish = new OrderDish();
            orderDish.setOrderId(orderId)
                    .setDishId(orderDishDTO.getDishId())
                    .setAmount(orderDishDTO.getAmount())
                    .setTotalPrice(orderDishDTO.getAmount() * dishService.selectDishPriceById(orderDishDTO.getDishId()));
            dishService.deductStock(orderDishDTO.getDishId(), orderDishDTO.getAmount());
            orderDishMapper.insert(orderDish);
        }
        // RabbitMQ发送订单消息
        String msg = orderDTO.getTable() + "号桌有新的订单";
        rabbitTemplate.convertAndSend("order.fanout", "", msg);
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
