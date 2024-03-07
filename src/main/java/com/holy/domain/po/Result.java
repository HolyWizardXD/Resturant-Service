package com.holy.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result<T> {

    // 业务状态码
    private int code;

    // 提示信息
    private String msg;

    // 响应数据
    private T data;

    // 快速返回操作成功响应结果(含有响应数据)
    public static <E> Result<E> success(E data) {
        return new Result<>(0,"操作成功",data);
    }

    // 快速返回操作成功响应结果(含有响应数据,含有提示信息)
    public static <E> Result<E> success(E data, String msg) {
        return new Result<>(0,msg,data);
    }

    // 快速返回操作成功响应结果(无响应数据)
    public static <E> Result<E> success() {
        return new Result<>(0,"操作成功",null);
    }

    // 快速返回操作失败响应结果(含有提示信息)
    public static <E> Result<E> error(String msg) {
        return new Result<>(1,msg,null);
    }
}
