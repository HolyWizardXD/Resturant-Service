package com.holy.common;

public class CommonField {

    // Redis 菜品缓存key String
    public final static String RedisDishKEY = "restaurant:dish:id:";

    // 用户正常启用
    public static final Integer NORMAL = 1;

    // 用户被冻结
    public static final Integer FROZEN = 0;
}
