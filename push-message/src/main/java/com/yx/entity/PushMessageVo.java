package com.yx.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangxl
 * @since 2019-12-20
 */
public class PushMessageVo implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     *主键id
     */
    private Integer id;
    
    //发送人用户id
    private Long sendUserId; 

    /**
     * 类型   1、日程  2、任务  3：通知公告 4：公文 8:提醒
     */
    private int type;
    //小类型
    private int objType; //日程来源   1 手机日程 、 2政通日程  3 政通分享   4笔记分享 5共享日程  6代建日程  7普通日程 8:任务  9:通知公告  10：公文

    /**
     * 内容
     */
    private String content;
    
  //标题
  	private String title;
  	
  	//对象主键
  	private String objId;
    
  //发送人用户人
    private String sendUserName; 
    
  //扩展内容
    private String extendContent;
    
  //接收人用户人
    private String receiveUserName;

    /**
     * 接收人用户id
     */
    private Long receiveUserId;

    /**
     * 来源 0:政通   1：其他
     */
    private Integer souceType;
    
    /**
     * 状态  0:已读   1：未读
     */
    private Integer state;
    
    
    /**
     * 消息类型    1： 督办   2：评价
     */
    private Integer msgType;
    
    /**
     * 消息数
     */
    private Integer msgCount;

    /**
     * url地址
     */
    private String url;

    /**
     * 创建时间
     */
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

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

	public int getObjType() {
		return objType;
	}

	public void setObjType(int objType) {
		this.objType = objType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
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

	public Long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(Long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	public Integer getSouceType() {
		return souceType;
	}

	public void setSouceType(Integer souceType) {
		this.souceType = souceType;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getMsgType() {
		return msgType;
	}

	public void setMsgType(Integer msgType) {
		this.msgType = msgType;
	}

	public Integer getMsgCount() {
		return msgCount;
	}

	public void setMsgCount(Integer msgCount) {
		this.msgCount = msgCount;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}
}
