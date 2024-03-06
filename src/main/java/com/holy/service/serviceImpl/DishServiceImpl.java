package com.holy.service.serviceImpl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.holy.domain.po.Dish;
import com.holy.mapper.DishMapper;
import com.holy.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    // 按照可选菜品名 最小价格 最高价格 分类 分页查询菜品
    @Override
    public IPage<Dish> list(Integer pageNum, Integer pageSize, String dishName,
                            Float minPrice, Float maxPrice, String classify) {
        // 创建IPage对象
        IPage<Dish> page = new Page<>(pageNum,pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 判断条件是否为空
        if (dishName != null) {
            // 菜品名
            lambdaQueryWrapper.like(Dish::getDishName, dishName);
        }
        if (classify != null) {
            // 分类
            lambdaQueryWrapper.eq(Dish::getClassify, classify);
        }
        if (minPrice != null && maxPrice != null) {
            // 价格区间
            lambdaQueryWrapper.between(Dish::getPrice, minPrice, maxPrice);
        } else if (maxPrice != null) {
            // 最大价格
            lambdaQueryWrapper.le(Dish::getPrice, maxPrice);
        }else if (minPrice != null) {
            // 最小价格
            lambdaQueryWrapper.ge(Dish::getPrice, minPrice);
        }
        // 调用mapper
        dishMapper.selectPage(page, lambdaQueryWrapper);
        return page;
    }

    // 查询所有菜品
    @Override
    public List<Dish> listAll() {
        return dishMapper.selectList(null);
    }
}
