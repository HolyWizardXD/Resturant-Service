package com.holy.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class JwtUtil {

    private static final String KEY = "holy";

    // 生成token并返回
    public static String getToken(Map<String,Object> claims, boolean persist) {
        // 持久token30天过期
        Date date = new Date(System.currentTimeMillis() + 1000L * 60 * 60 * 24 * 30);
        // 非持久token1天过期
        if(!persist){
            date = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24);
        }
        return JWT.create()
                .withClaim("claims",claims)
                .withExpiresAt(date) // 设置token是否过期
                .sign(Algorithm.HMAC256(KEY));
    }

    // 接收token,验证token,返回业务数据
    public static Map<String,Object> parseToken(String token) {
        return JWT.require(Algorithm.HMAC256(KEY))
                .build()
                .verify(token)
                .getClaim("claims")
                .asMap();
    }
}
