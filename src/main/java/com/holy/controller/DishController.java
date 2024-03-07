package com.holy.controller;

import cn.hutool.core.io.unit.DataSize;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.holy.domain.dto.DishDTO;
import com.holy.domain.po.Dish;
import com.holy.domain.po.Result;
import com.holy.service.DishService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.holy.common.CommonString.RedisDishKEY;


@Tag(name = "菜品相关接口")
@RestController
@RequestMapping("/dish")
public class DishController {

    @Value("${path.dish}")
    private String path;

    @Value("${path.default}")
    private String defaultUrl;

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
        Set<String> keys = stringRedisTemplate.keys(RedisDishKEY + "*");
        // 不存在 调用数据库
        if (keys.isEmpty()){
            dishes = dishService.listAll();
            // 存入Redis
            dishes.forEach(dish -> {
                // 用restaurant:dish:id:#{id}作为key,Dish转换为json作为value
                stringRedisTemplate.opsForValue().set(
                        RedisDishKEY + dish.getId()
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

    @PutMapping("updateDish")
    @Operation(summary = "修改菜品信息(不能包含图片)")
    public Result updateDish(@RequestBody @Valid DishDTO dishDTO) {
        // 根据id取出菜品
        Dish dish = dishService.selectDishById(dishDTO.getId());
        // 查询该菜品是否存在
        if(dish == null) return Result.error("该菜品不存在");
        // 封装回dish
        dish.setDishName(dishDTO.getDishName())
                .setClassify(dishDTO.getClassify())
                .setPrice(dishDTO.getPrice())
                .setStatus(dishDTO.getStatus())
                .setStock(dishDTO.getStock());
        // 修改菜品信息
        dishService.updateDish(dish);
        // 如果菜品启用 修改Redis中的该菜品信息
        if(dish.getStatus() == 1) {
            stringRedisTemplate.opsForValue().set(RedisDishKEY + dish.getId(), JSONUtil.toJsonStr(dish));
        }else {
            // 不启用则删除Redis缓存
            stringRedisTemplate.delete(RedisDishKEY + dish.getId());
        }
        return Result.success();
    }

    @PostMapping("addDish")
    @Operation(summary = "新增菜品接口(图片设为默认)")
    public Result addDish(@RequestBody @Valid DishDTO dishDTO) {
        // 根据菜品名查询菜品是否存在
        if (dishService.selectDishByName(dishDTO.getDishName()) != null) return Result.error("菜品已经存在");
        // 封装Dish 不设置id id自增
        Dish dish = new Dish();
        dish.setDishName(dishDTO.getDishName())
                .setClassify(dishDTO.getClassify())
                .setPictureUrl(defaultUrl)
                .setPrice(dishDTO.getPrice())
                .setStatus(dishDTO.getStatus())
                .setStock(dishDTO.getStock());
        // 新增菜品
        dishService.addDish(dish);
        // 如果菜品启用 添加到Redis中
        if(dish.getStatus() == 1) {
            stringRedisTemplate.opsForValue().set(RedisDishKEY + dish.getId(), JSONUtil.toJsonStr(dish));
        }
        return Result.success();
    }

    @PostMapping("upload")
    @Operation(summary = "修改菜品图片接口")
    public Result updateDishPictureUrl(MultipartFile file, @RequestParam int dishId){
        // 判断是否有该菜品
        if(dishService.selectDishById(dishId) == null) return Result.error("该菜品不存在");
        // 判断文件上传类型是否合法
        String contentType = file.getContentType();
        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png") && !contentType.equals("image/jpg")){
            return Result.error("文件类型错误");
        }
        // 判断文件大小是否过大
        DataSize maxsize = DataSize.ofMegabytes(2);
        if(file.getSize() > maxsize.toBytes()) return Result.error("文件大小超过2MB");
        // 设置图片路径
        String fileName = path + "dish_" + dishId + ".jpg";
        try {
            // 转存文件
            file.transferTo(new File(fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // 修改数据库文件地址
        dishService.updatePictureUrlById(dishId, fileName);
        // 取出修改后的菜品对象
        Dish dish = dishService.selectDishById(dishId);
        // 修改Redis缓存文件地址
        stringRedisTemplate.opsForValue().set(RedisDishKEY + dishId, JSONUtil.toJsonStr(dish));
        return Result.success();
    }

    @DeleteMapping("delete")
    @Operation(summary = "删除菜品接口")
    public Result deleteDish(@RequestParam int dishId) {
        // 判断是否有该菜品
        if(dishService.selectDishById(dishId) == null) return Result.error("该菜品不存在");
        // 删除数据库中该菜品
        dishService.deleteDishById(dishId);
        // 判断Redis缓存中该菜品是否存在
        if(stringRedisTemplate.opsForValue().get(RedisDishKEY + dishId) != null) {
            // 删除Redis缓存中该菜品
            stringRedisTemplate.delete(RedisDishKEY + dishId);
        }
        return Result.success();
    }
}
