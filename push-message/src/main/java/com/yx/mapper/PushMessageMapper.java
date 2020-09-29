package com.yx.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yx.entity.CalendarView;
import com.yx.entity.PushMessage;
import com.yx.entity.PushMessageVo;

public interface PushMessageMapper extends BaseMapper<PushMessage> {
	/**
	 * 根据用户查询推送信息
	 * 
	 * @param issueId
	 * @return
	 */
	@Select("select * from push_message where  receiveUserId = #{arg0}  and state =#{arg1}  ORDER BY issueId desc")
	List<PushMessage> getPushMessageByUserId(Integer userId, Integer state);
	
	
	
	
	/**
	 * 根据用户查询推送总数
	 * 
	 * @param issueId
	 * @return
	 */
	@Select("select count(1) from push_message where  receiveUserId = #{arg0}  and state =#{arg1}  and type !=3 ")
	long msgCount(Integer userId, Integer state);
	

	/**
	 * 修改推送信息
	 * 
	 * @param issueId
	 * @return
	 */
	@Update("update push_message set state=#{arg3} where  receiveUserId =#{arg0}  and objId =#{arg1}  and type=#{arg2}")
	void updataPushMessage(int userId, String objId, int type, int state);

	/**
	 * 修改推送信息
	 * 
	 * @param issueId
	 * @return
	 */
	@Update("update push_message set state=#{arg1} where  receiveUserId =#{arg0}")
	void updataPushMessages(int userId, int state);

	/**
	 * 根据用户查询推送信息
	 * 
	 * @param issueId
	 * @return
	 */
	@Select("SELECT 	am.memorandumId AS id,am.creatUserId,am.creatUserName,amo.userId,amo.userName,am.memorandumTitle AS title, am.memorandumUrl AS type,"
			+ " am.bTime,am.eTime,am.remindBTime FROM	app_memorandum am,	app_memorandobject amo WHERE am.memorandumId = amo.mId "
			+ " AND am.remindBTime IS NOT NULL AND am.remindBTime between STR_TO_DATE(#{arg0}, '%Y-%m-%d %H:%i:%S') and STR_TO_DATE(#{arg1}, '%Y-%m-%d %H:%i:%S')")
	List<CalendarView> getRemindList(String bTime, String eTime);

	/**
	 * 查询推送信息
	 * 
	 * @param page
	 * @param receiveUserId
	 * @param state
	 * @return
	 */
	public List<PushMessageVo> getListGroupObjId(Page<PushMessageVo> page, @Param("receiveUserId")int receiveUserId, @Param("state")int state);

	
	/**
	 * 查询推送信息
	 * 
	 * @param page
	 * @param receiveUserId
	 * @param state
	 * @return
	 */
	public List<PushMessageVo> getListGroup(Page<PushMessageVo> page, @Param("receiveUserId")int receiveUserId, @Param("state")int state);

	
	/**
	 * 根据objid查询推送信息
	 * 
	 * @param objId
	 * @return
	 */
	@Select("select * from push_message where  objId =#{arg0} and receiveUserId = #{arg1} and state=#{arg2} ORDER BY issueId desc")
	List<PushMessage> getPushMessageByObjId( String objId,String userId,String state);
	
}
