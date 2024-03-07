package com.holy;

import com.holy.domain.po.Dish;
import com.holy.utils.Md5Util;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.List;
import java.util.Map;

@SpringBootTest
class RestaurantApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void MD5Test() {
        System.out.println(Md5Util.getMD5String("admin"));
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void RedisTest(){
        int id = 1;
        String json = "s";
        ValueOperations<String, String> dishes = stringRedisTemplate.opsForValue();
        dishes.set("restaurant:dish:id:" + id, json);
    }
}
