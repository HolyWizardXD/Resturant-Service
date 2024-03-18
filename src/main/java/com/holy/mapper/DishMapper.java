package com.holy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.holy.domain.po.Dish;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {

    @Update("update dish set picture_url=#{pictureUrl} where id=#{id}")
    int updatePictureUrlById(int id,String pictureUrl);

    @Select("select stock from dish where id=#{id}")
    Integer selectDishStockById(Integer id);

    @Select("select price from dish where id=#{id}")
    float selectDishPriceById(Integer id);

    @Update("update dish set stock=stock-#{amount} where id=#{id}")
    Integer deductStock(Integer id, Integer amount);
}
