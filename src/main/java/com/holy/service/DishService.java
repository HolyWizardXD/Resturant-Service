package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Dish;

import java.util.List;

public interface DishService {

    IPage<Dish> list(Integer pageNum, Integer pageSize, String dishName, Float minPrice, Float maxPrice, String classify);

    List<Dish> listAll();
}
