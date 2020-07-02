package com.bo.enums;


/**
 * 
 * @Description: 发送消息的动作 枚举
 */
public enum MsgActionEnum {
	
	CONNECT(1, "第一次(或重连)初始化连接"),
	CHAT(2, "聊天消息"),	
	SIGNED(3, "消息签收"),
	KEEPALIVE(4, "客户端保持心跳"),
	NOTICEREQUEST(5, "申请好友、群组请求"),
	NEWFRIEND(6,"通知好友添加至列表"),
	ONLINE(7,"通知好友我在线了"),
	OFFLINE(8,"通知好友我离线了"),
	NEWGROUP(9,"通知好友刷新群列表"),
	ADMIN_CONNECT(10,"管理员连接"),
    REJECTUSER(11,"踢出用户"),
	SQUEEZE_OFFLINE(12,"您的账号已在其他地方登录");


    public final Integer type;
	public final String content;
	
	MsgActionEnum(Integer type, String content){
		this.type = type;
		this.content = content;
	}
	
	public Integer getType() {
		return type;
	}  
}
