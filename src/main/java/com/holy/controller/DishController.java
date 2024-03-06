package com.holy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.po.Dish;
import com.holy.domain.po.Result;
import com.holy.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Tag(name = "菜品相关接口")
@RestController
@RequestMapping("/dish")
public class DishController {

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
        // 调用菜品服务的全部查询
        return Result.success(dishService.listAll());
    }
}
