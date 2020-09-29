package com.yx.entity;

import java.io.Serializable;
import java.util.List;

public class EventView implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     * 类型   1、日程  2、任务  3：通知公告 4：公文 8:提醒
     */
    private int type;
    /**
     * 内容
     */
    private String content;
    
    //1 后台更新推送
  	private int method;
  	
  //对象主键
  	private List<String> objIds;


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

	public int getMethod() {
		return method;
	}

	public void setMethod(int method) {
		this.method = method;
	}

	public List<String> getObjIds() {
		return objIds;
	}

	public void setObjIds(List<String> objIds) {
		this.objIds = objIds;
	}
	

}
