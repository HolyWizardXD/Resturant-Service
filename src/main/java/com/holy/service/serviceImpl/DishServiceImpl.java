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
        IPage<Dish> page = new Page<>(pageNum, pageSize);
        // 创建条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 菜品状态为启用
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        // 判断条件是否为空
        if (dishName != null) {
            // 菜品名
            lambdaQueryWrapper.like(Dish::getDishName, dishName);
        }
        System.out.println(classify);
        if (classify != null && !classify.equals("")) {
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
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getStatus, 1);
        return dishMapper.selectList(lambdaQueryWrapper);
    }

    // 修改菜品
    @Override
    public boolean updateDish(Dish dish) {
        return dishMapper.updateById(dish) > 0;
    }

    // 通过id查询寻菜品
    @Override
    public Dish selectDishById(int id) {
        return dishMapper.selectById(id);
    }

    // 添加菜品
    @Override
    public boolean addDish(Dish dish) {
        return dishMapper.insert(dish) > 0;
    }

    // 修改或添加菜品图片
    @Override
    public boolean updatePictureUrlById(int id, String pictureUrl) {
        return dishMapper.updatePictureUrlById(id, pictureUrl) > 0;
    }
}
