package com.yx.config;

import javax.annotation.Resource;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.yx.service.PushMessageService;
@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
public class CronTask {
	@Resource
	 private PushMessageService pushMessageService;
	/*
	@Scheduled(cron = "0 0/1 * * * ?")
	public void cron() {
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		String currDate = formatter.format(date);
		System.out.println("当前时间：" +currDate); 		
		 List<CalendarView> list = pushMessageService.getRemindList(currDate + ":00", currDate+":59");
		 if(list != null && list.size()>0 ){
			 for(CalendarView view : list){
				 if(WebSocketHandler.map.containsKey(view.getUserId())){
						ChannelHandlerContext cxt =	WebSocketHandler.map.get(view.getUserId());
						PushMessage msg = new PushMessage();
			             msg.setContent(view.getTitle());
			             msg.setType(8);
			             msg.setSouceType(0);
			             msg.setObjId(view.getId());
			             msg.setExtendContent(JSON.toJSONString(view));
						String messageStr = JSON.toJSONString(msg);
						System.out.println("推送提醒：" + messageStr);
						cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
						//将推送消息存入数据库
						pushMessageService.save(msg);
					}else{
						System.out.println("不在线");
					}
			 }
		
		
	}
 }*/
}
