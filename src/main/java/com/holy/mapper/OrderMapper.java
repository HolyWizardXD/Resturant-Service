package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Order;
import com.holy.domain.vo.OrderVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    IPage<OrderVO> list(IPage<OrderVO> page, String customerName, LocalDateTime begin, LocalDateTime end);

    List<OrderVO> listAll(Integer customerId);

    int insertOrder(Order order);

    OrderVO selectByOrderId(Integer orderId);

    int updateOrderStatus(Integer id);

    /*
     废弃
     List<OrderVO> selectLimit(Integer pageSize, Integer offset, LocalDateTime begin, LocalDateTime end);
     int countOrder();
    */

}
