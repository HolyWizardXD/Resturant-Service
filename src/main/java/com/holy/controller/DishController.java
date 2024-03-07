package com.holy.controller;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Dish;
import com.holy.domain.po.Result;
import com.holy.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


@Tag(name = "菜品相关接口")
@RestController
@RequestMapping("/dish")
public class DishController {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private DishService dishService;

    @GetMapping("/list")
    @Operation(summary = "菜品列表")
    public Result<IPage<Dish>> list (
            Integer pageNum, Integer pageSize,
            @RequestParam(required = false) String dishName,
            @RequestParam(required = false) Float minPrice,
            @RequestParam(required = false) Float maxPrice,
            @RequestParam(required = false) String classify
            ){
        // 调用菜品服务的分页查询
        IPage<Dish> iPage = dishService.list(pageNum, pageSize, dishName, minPrice, maxPrice, classify);
        return Result.success(iPage);
    }

    @GetMapping("/listAll")
    @Operation(summary = "全部菜品")
    public Result<List<Dish>> listAll(){
        List<Dish> dishes = new ArrayList<>();
        // 查询以restaurant:dish:id:开头的Redis缓存
        Set<String> keys = stringRedisTemplate.keys("restaurant:dish:id:*");
        // 不存在 调用数据库
        if (keys.isEmpty()){
            dishes = dishService.listAll();
            // 存入Redis
            dishes.forEach(dish -> {
                // 用restaurant:dish:id:#{id}作为key,Dish转换为json作为value
                stringRedisTemplate.opsForValue().set(
                        "restaurant:dish:id:" + dish.getId()
                        ,JSONUtil.toJsonStr(dish)
                        ,6, TimeUnit.HOURS);
            });
        } else {
            // 存在 取出Redis缓存
            List<String> strDishes = stringRedisTemplate.opsForValue().multiGet(keys);
            // 依此存入dishes列表
            for (String json : strDishes) {
                // 取出json字符串转换为Dish对象存入dishes
                dishes.add(JSONUtil.toBean(json, Dish.class));
            }
        }
        // 返回全部菜品 使用stream流进行排序
        return Result.success(dishes.stream()
                .sorted(Comparator.comparingDouble(Dish::getId))
                .collect(Collectors.toList()));
    }
}
