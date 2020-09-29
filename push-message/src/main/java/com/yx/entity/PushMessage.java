package com.yx.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author yangxl
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class PushMessage implements Serializable {

    private static final long serialVersionUID = 1L;
    
    /**
     *主键id
     */
    @TableId(value = "issueId", type = IdType.AUTO)
    private Integer issueId;
    
    //发送人用户id
    @TableField("sendUserId")
    private Long sendUserId; 

    /**
     * 类型   1、日程  2、任务  3：通知公告 4：公文 8:提醒
     */
    @TableField("type")
    private int type;
    //小类型
    @TableField("objType")
    private int objType; //日程来源   1 手机日程 、 2政通日程  3 政通分享   4笔记分享 5共享日程  6代建日程  7普通日程 8:任务  9:通知公告  10：公文

    /**
     * 内容
     */
    @TableField("content")
    private String content;
    
  //标题
    @TableField("title")
  	private String title;
  	
  	//对象主键
    @TableField("objId")
  	private String objId;
    
  //发送人用户人
    @TableField("sendUserName")
    private String sendUserName; 
    
  //扩展内容
    @TableField("extendContent")
    private String extendContent;
    
  //接收人用户人
    @TableField("receiveUserName")
    private String receiveUserName;

    /**
     * 接收人用户id
     */
    @TableField("receiveUserId")
    private Long receiveUserId;

    /**
     * 来源 0:政通   1：其他
     */
    @TableField("souceType")
    private Integer souceType;
    
    /**
     * 状态  0:未读   1：已读
     */
    @TableField("state")
    private Integer state;
    
    
    /**
     * 消息类型    1： 督办   2：评价
     */
    @TableField("msgType")
    private Integer msgType;

    /**
     * url地址
     */
    @TableField("url")
    private String url;

    /**
     * 创建时间
     */
    @TableField("createDate")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createDate;
}
