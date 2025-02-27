package com.holy;

import com.holy.utils.EncryptUtil;
import com.holy.utils.Md5Util;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
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
    }

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Test
    void rabbitTest() throws InterruptedException {
        for (int i = 1; i <= 100; i++) {
            Thread.sleep(1000);
            rabbitTemplate.convertAndSend("order.fanout", "", i +"号桌有新的订单");
        }
    }

    @Test
    void testEncryptPhone() {
        String phone = "17616544400";
        System.out.println(EncryptUtil.encryptPhone(phone));
    }
}
