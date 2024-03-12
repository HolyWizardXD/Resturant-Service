package com.holy;

import com.holy.utils.Md5Util;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static com.holy.common.CommonField.RedisDishKEY;

@SpringBootTest
class RestaurantApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void MD5Test() {
        System.out.println(Md5Util.getMD5String("111222"));
    }

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void RedisTest(){
        int id = 1;
        String json = "s";
        ValueOperations<String, String> dishes = stringRedisTemplate.opsForValue();
        dishes.set(RedisDishKEY + id, json);
    }
}
