package com.yx.entity;

import java.io.Serializable;

public class CalendarView implements Serializable{
	/**
	 * 日历列表
	 */
	private static final long serialVersionUID = 1L;
	private String creatUserId;//创建人主键
	private String creatUserName;//创建人姓名
	private String userId;
	private String userName;
	private String id;//主键
	private String title; //标题
	private String bTime; //开始时间
	private String eTime; //结束时间
	private String remindBTime; //提醒时间
	private String creatTime;//创建时间
	private int type;//日程来源   1 手机日程 、 2政通日程  3 政通分享   4笔记分享 5共享日程  6代建日程  7普通日程 
	                    //任务的类型---1：督件:2：普通
	private int  duCount = 0;//督办次数
	private String  state;  //状态； 0 未读；1已读  
	private int ugency=1;//紧急程度   3:特急、2:紧急、1、常规
	private String duUserName; //督办人,多个逗号分隔
	private int calendarType=0; //日历类型  1、日程  2、任务  3：通知公告 4：公文
	private int top=0; //是否有置顶排序
	
	public String getCreatUserId() {
		return creatUserId;
	}
	public void setCreatUserId(String creatUserId) {
		this.creatUserId = creatUserId;
	}
	public String getCreatUserName() {
		return creatUserName;
	}
	public void setCreatUserName(String creatUserName) {
		this.creatUserName = creatUserName;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getbTime() {
		return bTime;
	}
	public void setbTime(String bTime) {
		this.bTime = bTime;
	}
	public String geteTime() {
		return eTime;
	}
	public void seteTime(String eTime) {
		this.eTime = eTime;
	}
	public String getCreatTime() {
		return creatTime;
	}
	public void setCreatTime(String creatTime) {
		this.creatTime = creatTime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getDuCount() {
		return duCount;
	}
	public void setDuCount(int duCount) {
		this.duCount = duCount;
	}
	public int getUgency() {
		return ugency;
	}
	public void setUgency(int ugency) {
		this.ugency = ugency;
	}
	public String getDuUserName() {
		return duUserName;
	}
	public void setDuUserName(String duUserName) {
		this.duUserName = duUserName;
	}
	public int getCalendarType() {
		return calendarType;
	}
	public void setCalendarType(int calendarType) {
		this.calendarType = calendarType;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public String getRemindBTime() {
		return remindBTime;
	}
	public void setRemindBTime(String remindBTime) {
		this.remindBTime = remindBTime;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
}
