package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.vo.OrderVO;
import com.holy.mapper.OrderMapper;
import com.holy.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public IPage<OrderVO> list(Integer pageNum, Integer pageSize, LocalDateTime begin, LocalDateTime end) {
        // 创建IPage对象
        IPage<OrderVO> iPage = new Page<>(pageNum, pageSize);
        // 把iPage传入Mapper 自动拦截分页
        iPage = orderMapper.list(iPage, begin, end);
        return iPage;
    }

    @Override
    public List<OrderVO> selectById(Integer customerId) {
        // 查询该顾客所有订单
        return orderMapper.listAll(customerId);
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
