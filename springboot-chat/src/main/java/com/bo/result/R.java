package com.bo.result;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)//允许链式
public class R {
    private Boolean success;
    private Integer code;
    private String message;
    private Map<String,Object> data = new HashMap<>();

    //私有化构造器
    private R(){}

    //通用返回成功
    public static R ok(){
        return new R()
                .setSuccess(ResultCodeEnum.SUCCESS.getSuccess())
                .setMessage(ResultCodeEnum.SUCCESS.getMessage())
                .setCode(ResultCodeEnum.SUCCESS.getCode());
    }

    //通用返回错误
    public static R error(){
        return new R()
                .setSuccess(ResultCodeEnum.UNKNOWN_ERROR.getSuccess())
                .setMessage(ResultCodeEnum.UNKNOWN_ERROR.getMessage())
                .setCode(ResultCodeEnum.UNKNOWN_ERROR.getCode());
    }

    //设置结果，形参为结果枚举
    public static R setResult(ResultCodeEnum result){
        return new R()
                .setSuccess(result.getSuccess())
                .setMessage(result.getMessage())
                .setCode(result.getCode());
    }

    //自定义返回数据
    public R data(Map<String,Object> map){
        this.setData(map);
        return this;
    }

    //通用设置data
    public R data(String key,Object value){
        this.data.put(key,value);
        return this;
    }

    //自定义状态信息
    public R message(String message){
        this.setMessage(message);
        return this;
    }

    //自定义状态码
    public R code(Integer code){
        this.setCode(code);
        return this;
    }

    //自定义返回结果
    public R success(Boolean success){
        this.setSuccess(success);
        return this;
    }

}
