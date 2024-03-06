package com.holy.exception;

import com.holy.domain.po.Result;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BindException.class)
    public Result BindExceptionHandler(BindException e)
    {
        //捕获数据校验异常
        Result result = new Result();
        result.setCode(500);
        //获取实体类定义的校验注解字段上的message作为异常信息，@NotBlank(message = "用户密码不能为空！")异常信息即为"用户密码不能为空！"
        result.setMsg(e.getBindingResult().getFieldError().getDefaultMessage());
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e)
    {

        //捕获数据校验异常
        Result result = new Result();
        result.setCode(600);
        //获取实体类定义的校验注解字段上的message作为异常信息，@NotBlank(message = "用户密码不能为空！")异常信息即为"用户密码不能为空！"
        result.setMsg(e.getBindingResult().getFieldError().getDefaultMessage());
        return result;
    }

    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e){
        e.printStackTrace();
        return Result.error(StringUtils.hasLength(e.getMessage())? e.getMessage() : "操作失败");
    }

}
