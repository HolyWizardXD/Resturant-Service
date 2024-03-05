package com.holy;

import com.holy.utils.Md5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RestaurantApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void MD5Test() {
        System.out.println(Md5Util.getMD5String("admin"));
    }
}
