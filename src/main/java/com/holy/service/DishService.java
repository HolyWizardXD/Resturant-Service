package com.holy.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Dish;

import java.util.List;

public interface DishService {

    IPage<Dish> list(Integer pageNum, Integer pageSize, String dishName, String classify);

    List<Dish> listAll();

    boolean updateDish(Dish dish);

    Dish selectDishById(int id);

    boolean addDish(Dish dish);

    boolean updatePictureUrlById(int id, String pictureUrl);

    boolean deleteDishById(int dishId);

    Dish selectDishByName(String dishName);

    Integer selectDishStockById(Integer dishId);

    float selectDishPriceById(Integer dishId);

    Integer deductStock(Integer dishId, Integer amount);

    boolean updateDishStatus(Integer id, Integer status);
}
