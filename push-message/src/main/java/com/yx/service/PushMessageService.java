package com.yx.service;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yx.entity.CalendarView;
import com.yx.entity.PushMessage;
import com.yx.entity.PushMessageVo;

public interface PushMessageService extends IService<PushMessage>{

	/**
     * 根据用户查询推送信息
     * @param issueId
     * @return
     */
    List<PushMessage> getPushMessageByUserId(int receiveUserId,int state);
    
    /**
	 * 根据用户查询推送总数
	 * 
	 * @param issueId
	 * @return
	 */
    long msgCount(Integer userId, Integer state);
    
    
    
    /**
     * 根据用户查询推送信息
     * @param issueId
     * @return
     */
    List<PushMessageVo> getListGroupObjId( Page<PushMessageVo> page,int receiveUserId,int state);
    
    
    
    /**
     * 根据用户查询推送信息
     * @param issueId
     * @return
     */
    List<PushMessageVo> listGroup( Page<PushMessageVo> page,int receiveUserId,int state);
    
    
    /**
     * 修改推送信息
     * @param issueId
     * @return
     */
    void updataPushMessage(int userId,String objId,int type,int state);
    
    /**
     * 修改推送信息
     * @param issueId
     * @return
     */
    void updataPushMessages(int userId,int state);
    
    /**
     * 查询提醒数据
     * @param bTime
     * @param eTime
     * @return
     */
    List<CalendarView> getRemindList(String bTime,String eTime);
    
    /**
	 * 根据objid查询推送信息
	 * 
	 * @param objId
	 * @return
	 */
	List<PushMessage> getPushMessageByObjId( String objId,String userId,String state);
}
