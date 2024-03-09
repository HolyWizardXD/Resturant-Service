package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Order;
import com.holy.domain.vo.OrderVO;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface OrderMapper extends BaseMapper<Order> {
    IPage<OrderVO> list(IPage<OrderVO> page, LocalDateTime begin, LocalDateTime end);

    List<OrderVO> listAll(Integer customerId);

    /*
     废弃
     List<OrderVO> selectLimit(Integer pageSize, Integer offset, LocalDateTime begin, LocalDateTime end);
     int countOrder();
    */

}
