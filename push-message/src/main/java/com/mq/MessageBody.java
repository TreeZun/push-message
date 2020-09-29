package com.mq;

import java.io.Serializable;

public class MessageBody implements  Serializable{
	/**
	 * 发送信息体
	 */
	private static final long serialVersionUID = 1L;
	//发送人用户id
    private Long sendUserId; 
    
    //发送人用户人
    private String sendUserName; 
    //类型   1、日程  2、任务  3：通知公告 4：公文
    private int type;
    //内容
    private String content;
    
    //扩展内容
    private String extendContent;
    
	//标题
	private String title;
	
	//对象主键
	private String objId;
    
    //接收人用户id
    private Long receiveUserId;
    
  //接收人用户人
    private String receiveUserName; 
    
    //来源 0:政通   1：其他
    private int souceType;
    
    //日程来源   1 手机日程 、 2政通日程  3 政通分享   4笔记分享 5共享日程  6代建日程  7普通日程 8:任务  9:通知公告  10：公文
    private int objType; 
    
  //url地址
   private String url;
   
   /**
    * 消息类型    1： 督办   2：评价
    */
   private Integer msgType;

	public Long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(Long sendUserId) {
		this.sendUserId = sendUserId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public int getSouceType() {
		return souceType;
	}

	public void setSouceType(int souceType) {
		this.souceType = souceType;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getObjId() {
		return objId;
	}

	public void setObjId(String objId) {
		this.objId = objId;
	}

	@Override
	public String toString() {
		return "PushMessageBody [sendUserId=" + sendUserId + ", type=" + type + ", content=" + content + ", title="
				+ title + ", objId=" + objId + ", receiveUserId=" + receiveUserId + ", souceType=" + souceType
				+ ", url=" + url + "]";
	}

	public String getSendUserName() {
		return sendUserName;
	}

	public void setSendUserName(String sendUserName) {
		this.sendUserName = sendUserName;
	}

	public String getExtendContent() {
		return extendContent;
	}

	public void setExtendContent(String extendContent) {
		this.extendContent = extendContent;
	}

	public String getReceiveUserName() {
		return receiveUserName;
	}

	public void setReceiveUserName(String receiveUserName) {
		this.receiveUserName = receiveUserName;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

}

