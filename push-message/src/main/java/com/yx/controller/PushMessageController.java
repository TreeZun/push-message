package com.yx.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yx.entity.ErrCode;
import com.yx.entity.EventView;
import com.yx.entity.PushMessage;
import com.yx.entity.PushMessageVo;
import com.yx.entity.RestResult;
import com.yx.handler.WebSocketHandler;
import com.yx.service.PushMessageService;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

@RequestMapping("/push")
@RestController
public class PushMessageController {
	@Resource
	 private PushMessageService pushMessageService;
	private static final Logger logger = LoggerFactory.getLogger(PushMessageController.class);

	
	/**
	 * 查询推送列表
	 * @param receiveUserId
	 * @param state
	 * @return
	 */
	 @RequestMapping(value = "/list", method = RequestMethod.GET)
	 @ResponseBody
	public RestResult  list(String receiveUserId, String state){
		 RestResult restResult = new RestResult();
		  if (StringUtils.isEmpty(receiveUserId)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  if (StringUtils.isEmpty(state)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  List<PushMessage> list =  pushMessageService.getPushMessageByUserId(Integer.parseInt(receiveUserId), Integer.parseInt(state));
		  restResult.setData(list);
		  return restResult;
	}
	 
	 /**
		 * 查询推送列表
		 * @param receiveUserId
		 * @param state
		 * @return
		 */
		 @RequestMapping(value = "/list/{objId}/{userId}/{state}", method = RequestMethod.GET)
		 @ResponseBody
		public RestResult  getMesage(@PathVariable String objId, @PathVariable String userId, @PathVariable String state){
			 RestResult restResult = new RestResult();
			  if (StringUtils.isEmpty(objId)) {
		            restResult.setResult(ErrCode.PARAMETER_WRONG);
		            return restResult;
		        }
			 
			  List<PushMessage> list =  pushMessageService.getPushMessageByObjId(objId,userId,state);
			  restResult.setData(list);
			  return restResult;
		}
	 
	 
	 /**
		 * 查询推送列表
		 * @param receiveUserId
		 * @param state
		 * @return
		 */
		 @RequestMapping(value = "/listGroupObjId", method = RequestMethod.GET)
		 @ResponseBody
		public RestResult  listGroupObjId(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
                @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,String receiveUserId, String state){
			 RestResult restResult = new RestResult();
			  if (StringUtils.isEmpty(receiveUserId)) {
		            restResult.setResult(ErrCode.PARAMETER_WRONG);
		            return restResult;
		        }
			  if (StringUtils.isEmpty(state)) {
		            restResult.setResult(ErrCode.PARAMETER_WRONG);
		            return restResult;
		        }
			  Page<PushMessageVo> page = new Page<>(currentPage,pageSize);
			  List<PushMessageVo> list =  pushMessageService.getListGroupObjId(page,Integer.parseInt(receiveUserId), Integer.parseInt(state));
			  page.setRecords(list);
			  restResult.setData(page);
			  return restResult;
		}
		 
		 
		
			 
			 
		 
	/**
	 * 修改推送信息状态
	 * @param receiveUserId
	 * @param objId
	 * @param type
	 * @param state
	 * @return
	 */
	 @RequestMapping(value = "/updataPushMessage", method = RequestMethod.POST)
	 @ResponseBody
	public RestResult  updataPushMessage(String receiveUserId,String objId,String type,String state){
		 RestResult restResult = new RestResult();
		  if (StringUtils.isEmpty(receiveUserId)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  if (StringUtils.isEmpty(state)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  if (StringUtils.isEmpty(type)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  if (StringUtils.isEmpty(objId)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		   pushMessageService.updataPushMessage(Integer.parseInt(receiveUserId), objId, Integer.parseInt(type), Integer.parseInt(state));
		 
		   List<String> list = new ArrayList();
		   list.add(objId);
		  if( WebSocketHandler.map.containsKey(receiveUserId +"_android")){
			  ChannelHandlerContext cxt =	WebSocketHandler.map.get(receiveUserId +"_android");
			  EventView view = new EventView();
			  view.setType(Integer.parseInt(type));
			  view.setObjIds(list);
			  view.setMethod(1);
			 String messageStr = JSON.toJSONString(view);
			 cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
			 logger.info("android用户："+receiveUserId + " 在线，发送后台推送消息：" + messageStr);
		  }
		  
		  if( WebSocketHandler.map.containsKey(receiveUserId +"_ios")){
			  ChannelHandlerContext cxt =	WebSocketHandler.map.get(receiveUserId +"_ios");
			  EventView view = new EventView();
			  view.setType(Integer.parseInt(type));
			  view.setObjIds(list);
			  view.setMethod(1);
			 String messageStr = JSON.toJSONString(view);
			 cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
			 logger.info("ios用户："+receiveUserId + " 在线，发送后台推送消息：" + messageStr);
		  }
		  
		   
		   return restResult;
	}
	 
	 /**
	  * 修改用户通知状态
	  * @param receiveUserId
	  * @param state
	  * @return
	  */
	 @RequestMapping(value = "/updataPushMessages", method = RequestMethod.POST)
	 @ResponseBody
	public RestResult  updataPushMessage(String receiveUserId,String state){
		 RestResult restResult = new RestResult();
		  if (StringUtils.isEmpty(receiveUserId)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		  if (StringUtils.isEmpty(state)) {
	            restResult.setResult(ErrCode.PARAMETER_WRONG);
	            return restResult;
	        }
		 
		 
		   List<PushMessage> list =  pushMessageService.getPushMessageByUserId(Integer.parseInt(receiveUserId), Integer.parseInt(state));
		   List<String> objIds = new ArrayList();
		   if(list != null && list.size()>0){
			   for(PushMessage pushMessage : list){
				   objIds.add(pushMessage.getObjId());
			   }
		   }
		   
		  
		  if( WebSocketHandler.map.containsKey(receiveUserId +"_android")){
			  ChannelHandlerContext cxt =	WebSocketHandler.map.get(receiveUserId +"_android");
			  EventView view = new EventView();
			  view.setObjIds(objIds);
			  view.setMethod(1);
			 String messageStr = JSON.toJSONString(view);
			 cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
			 logger.info("android用户："+receiveUserId + " 在线，发送后台推送消息：" + messageStr);
		  }
		  
		  if( WebSocketHandler.map.containsKey(receiveUserId +"_ios")){
			  ChannelHandlerContext cxt =	WebSocketHandler.map.get(receiveUserId +"_ios");
			  EventView view = new EventView();
			  view.setObjIds(objIds);
			  view.setMethod(1);
			 String messageStr = JSON.toJSONString(view);
			 cxt.writeAndFlush(new TextWebSocketFrame(messageStr) );
			 logger.info("ios用户："+receiveUserId + " 在线，发送后台推送消息：" + messageStr);
		  }
		  
		  
		  pushMessageService.updataPushMessages(Integer.parseInt(receiveUserId), Integer.parseInt(state));
		   
		  
		  return restResult;
	}
	 
	 /**
		 * 查询推送列表(不查询通知公告)
		 * @param receiveUserId
		 * @param state
		 * @return
		 */
		 @RequestMapping(value = "/listGroup", method = RequestMethod.GET)
		 @ResponseBody
		public RestResult  listGroup(@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
             @RequestParam(value = "pageSize", defaultValue = "10") int pageSize,String receiveUserId, String state){
			 RestResult restResult = new RestResult();
			  if (StringUtils.isEmpty(receiveUserId)) {
		            restResult.setResult(ErrCode.PARAMETER_WRONG);
		            return restResult;
		        }
			  if (StringUtils.isEmpty(state)) {
		            restResult.setResult(ErrCode.PARAMETER_WRONG);
		            return restResult;
		        }
			  Page<PushMessageVo> page = new Page<>(currentPage,pageSize);
			  List<PushMessageVo> list =  pushMessageService.listGroup(page,Integer.parseInt(receiveUserId), Integer.parseInt(state));
			  page.setRecords(list);
			  restResult.setData(page);
			  return restResult;
		}
		 
		 /**
			 * 查询推送列表总数
			 * @param receiveUserId
			 * @param state
			 * @return
			 */
			 @RequestMapping(value = "/megCount", method = RequestMethod.GET)
			 @ResponseBody
			public RestResult  megCount(String receiveUserId, String state){
				 RestResult restResult = new RestResult();
				  if (StringUtils.isEmpty(receiveUserId)) {
			            restResult.setResult(ErrCode.PARAMETER_WRONG);
			            return restResult;
			        }
				  if (StringUtils.isEmpty(state)) {
			            restResult.setResult(ErrCode.PARAMETER_WRONG);
			            return restResult;
			        }
				  long count =  pushMessageService.msgCount(Integer.parseInt(receiveUserId), Integer.parseInt(state));
				  restResult.setData(count);
				  return restResult;
			}
		 
			 
}
