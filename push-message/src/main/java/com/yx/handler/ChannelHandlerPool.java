package com.yx.handler;

import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * MyChannelHandlerPool
 * 通道组池，管理所有websocket连接
 * @author 杨小龙
 * @date 2020-04-21
 */
public class ChannelHandlerPool {

    public ChannelHandlerPool(){}

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

}