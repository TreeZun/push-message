package com.yx.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import com.alibaba.fastjson.JSON;
import com.utils.PoolManager;
import com.utils.SpringUtils;
import com.yx.entity.PushMessage;
import com.yx.service.PushMessageService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * WebSocketHandler WebSocket处理器，处理websocket连接相关
 * 
 * @author 杨小龙
 * @date 2020-04-21
 */
public class WebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
	// 存放用户连接
	public final static Map<String, ChannelHandlerContext> map = new HashMap<String, ChannelHandlerContext>();

	public final static Map<String, String> channelMap = new HashMap<String, String>();
	
	 //操作系统识别的换行符
//    private static final String CR=System.getProperty("line.separator");

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("与客户端建立连接，通道开启！" + ctx.channel());
		// 添加到channelGroup通道组
		ChannelHandlerPool.channelGroup.add(ctx.channel());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("与客户端断开连接，通道关闭！" + ctx.channel());
		// 移除在线人员
		if (channelMap.containsKey(ctx.channel().id().toString())) {
			map.remove(channelMap.get(ctx.channel().id().toString()));
			channelMap.remove(ctx.channel().id().toString());
		}
		// 添加到channelGroup 通道组
		ChannelHandlerPool.channelGroup.remove(ctx.channel().id());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 首次连接是FullHttpRequest，处理参数 by
		if (null != msg && msg instanceof FullHttpRequest) {
			FullHttpRequest request = (FullHttpRequest) msg;
			String uri = request.uri();

			Map paramMap = getUrlParams(uri);
			System.out.println("接收到的参数是：" + JSON.toJSONString(paramMap));
			map.put(paramMap.get("uid").toString(), ctx);
			channelMap.put(ctx.channel().id().toString(), paramMap.get("uid").toString());
			// 如果url包含参数，需要处理
			if (uri.contains("?")) {
				String newUri = uri.substring(0, uri.indexOf("?"));
				System.out.println(newUri);
				request.setUri(newUri);
			}

			PoolManager.getInstance().getThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					try {
						// 先休眠2秒钟
						Thread.sleep(2000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					pushCachMessage(ctx, paramMap);

				}
			});

		} else if (msg instanceof TextWebSocketFrame) {
			// 正常的TEXT消息类型
			TextWebSocketFrame frame = (TextWebSocketFrame) msg;
			System.out.println("客户端收到服务器数据：" + frame.text());
			// ctx.writeAndFlush(new TextWebSocketFrame(frame.text()) );
			sendAllMessage(frame.text());
		}
		super.channelRead(ctx, msg);
	
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame)
			throws Exception {

	}

	private void sendAllMessage(String message) {
		// 收到信息后，群发给所有channel
		ChannelHandlerPool.channelGroup.writeAndFlush(new TextWebSocketFrame(message));
	}

	private static Map getUrlParams(String url) {
		Map<String, String> map = new HashMap<>();
		url = url.replace("?", ";");
		if (!url.contains(";")) {
			return map;
		}
		if (url.split(";").length > 0) {
			String[] arr = url.split(";")[1].split("&");
			for (String s : arr) {
				String key = s.split("=")[0];
				String value = s.split("=")[1];
				map.put(key, value);
			}
			return map;

		} else {
			return map;
		}
	}

	private void pushCachMessage(ChannelHandlerContext ctx, Map paramMap) {
		try {
			RedisTemplate redisTemplate = (RedisTemplate) SpringUtils.getBeanByName("redisTemplate");
			PushMessageService pushMessageService = (PushMessageService) SpringUtils
					.getBeanByName("pushMessageService");
			ListOperations operation = redisTemplate.opsForList();
			// 连接成功先检查redis有没有推送信息
			List<PushMessage> listMessage = new ArrayList<PushMessage>();
			if(redisTemplate.hasKey(paramMap.get("uid").toString() + "_push_message")) {
				String value = redisTemplate.opsForValue().get(paramMap.get("uid").toString() + "_push_message").toString();
				Map  map = new HashMap<>();
				map.put("type", "-1");
				map.put("unreadnum", value);
			//	ctx.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(map)));
				redisTemplate.delete(paramMap.get("uid").toString() + "_push_message");
			}
			/*while (redisTemplate.hasKey(paramMap.get("uid").toString() + "_push_message")) {
				String value = redisTemplate.opsForValue().get(phone);
				Object strJson = operation.rightPop(paramMap.get("uid").toString() + "_push_message", 10,
						TimeUnit.SECONDS);
				// 推送消息
				ctx.writeAndFlush(new TextWebSocketFrame(strJson.toString()));
				PushMessage message = JSONObject.toJavaObject(JSON.parseObject(strJson.toString()),
						PushMessage.class);
				listMessage.add(message);
				//批量存入
				if(listMessage.size() >=50){
				pushMessageService.saveBatch(listMessage);
				listMessage =new ArrayList<PushMessage>();
				}
				Thread.sleep(2000);
			}
			if(listMessage !=null && listMessage.size() >0){
				pushMessageService.saveBatch(listMessage);
			}*/
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}