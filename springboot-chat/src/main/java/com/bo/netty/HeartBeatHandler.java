package com.bo.netty;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * 用于检测Channel的心跳Handler
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        //判断evt是否是IdleStateEvent(用于触发用户事件，包含 读空闲/写空闲/读写空闲)
        if (evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;

            switch (event.state()){
                case READER_IDLE:
                    System.out.println("进入读空闲");
                    break;
                case WRITER_IDLE:
                    System.out.println("进入写空闲");
                    break;
                case ALL_IDLE:
                    System.out.println("channel关闭前,users的数量为："+ChatHandler.users.size());
                    Channel channel = ctx.channel();
                    //关闭无用的channel，以防资源浪费
                    channel.close();
                    System.out.println("channel关闭后,users的数量为："+ChatHandler.users.size());
            }
        }
    }
}
