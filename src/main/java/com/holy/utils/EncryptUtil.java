package com.holy.utils;

public class EncryptUtil {

    public static String encryptPhone(String phone){
        return phone.substring(0,3) + "******" + phone.substring(9);
    }
}
