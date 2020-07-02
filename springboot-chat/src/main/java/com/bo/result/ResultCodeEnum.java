package com.bo.result;

import lombok.Getter;

//结果类枚举
@Getter
public enum ResultCodeEnum {

    SUCCESS(true,10000,"成功"),
    UNKNOWN_ERROR(false,10001,"未知错误"),
    PARAM_ERROR(false, 10002, "参数错误"),
    NULL_POINT_ERROR(false,10003,"空指针异常"),
    HTTP_CLIENT_ERROR(false,10004,"客户端异常"),
    BIND_ERROR(false, 10005, "绑定异常:%s"),

    // 用户模块
    PASSWORD_ERROR(false,20001,"密码错误"),
    USER_NOT_EXITST(false,20002,"用户不存在"),
    CAN_NOT_ADD_SELF(false,20003,"无法添加自己"),
    IS_EXIST_FRIEND(false,20004,"不得重复添加好友"),
    REPEAT_REQUEST(false,20004,"已发送好友申请"),
    GROUP_NOT_EXITST(false,20005,"群组不存在"),
    REPEAT_ADD_GROUP_REQUEST(false,20006,"不得重复添加群组"),
    REPEAT_GROUP_REQUEST(false,20007,"不得重复发送请求"),
    USER_WAS_FORBIDDEN(false,20008,"该用户被禁用")
    ;

    //响应是否成功
    private Boolean success;
    //响应状态码
    private Integer code;
    //响应信息
    private String message;

    ResultCodeEnum(boolean success,Integer code,String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }


}
