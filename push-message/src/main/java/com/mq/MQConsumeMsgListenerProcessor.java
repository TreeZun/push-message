package com.mq;

import java.util.List;

import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yx.entity.PushMessage;
import com.yx.handler.WebSocketHandler;
import com.yx.service.PushMessageService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
@Component 
//@Configuration
@EnableAutoConfiguration
public class MQConsumeMsgListenerProcessor implements MessageListenerConcurrently{
	@Value("${rocketmq.consumer.topics}")
    private String topics;
	
	private static final Logger logger = LoggerFactory.getLogger(MQConsumeMsgListenerProcessor.class);

	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
    private PushMessageService pushMessageService;
    

	@Override
	public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext arg1) {
		if(CollectionUtils.isEmpty(msgs)){
			logger.info("接收到的消息为空，不做任何处理");
			return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
		}
		MessageExt messageExt = msgs.get(0);
		String msg = new String(messageExt.getBody());
		ListOperations operation = redisTemplate.opsForList();
		logger.info("接收到的消息是："+msg);
		if(messageExt.getTopic().equals("push-topic")){
			if(messageExt.getTags().equals("push-tag")){
				int reconsumeTimes = messageExt.getReconsumeTimes();
				if(reconsumeTimes == 3){
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}
				MessageBody messageBody = JSONObject.parseObject(msg,MessageBody.class);
				if(messageBody !=null && messageBody.getReceiveUserId() !=0){
					if(WebSocketHandler.map.containsKey(messageBody.getReceiveUserId().toString())){
						ChannelHandlerContext cxt =	WebSocketHandler.map.get(messageBody.getReceiveUserId().toString());
						String messageStr = JSON.toJSONString(messageBody);
						cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
						PushMessage message = JSONObject.toJavaObject(JSON.parseObject(messageStr),PushMessage.class);
						//将推送消息存入数据库
						pushMessageService.save(message);
					}else{
			        //当前用户不存在就放在redis中
					System.out.println("当前用户不在线"+msg);
					String messageStr = JSON.toJSONString(messageBody);
					PushMessage message = JSONObject.toJavaObject(JSON.parseObject(messageStr),PushMessage.class);
					//将推送消息存入数据库
					pushMessageService.save(message);
				//	long count = this.redisTemplate.opsForValue().increment(messageBody.getReceiveUserId() + "_push_message", 1L).longValue();
					/*
					RedisAtomicLong entityIdCounter = new RedisAtomicLong(messageBody.getReceiveUserId()+"_push_message", redisTemplate.getConnectionFactory());
					Long increment = entityIdCounter.getAndIncrement();*/
                  //  System.out.println("未读数：" + count);
			
					//operation.leftPush(messageBody.getReceiveUserId()+"_push_message", JSON.toJSONString(messageBody));
				}
				}
			}
		}
		return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
	}


}
