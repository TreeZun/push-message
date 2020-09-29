package com.yx.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yx.entity.CalendarView;
import com.yx.entity.PushMessage;
import com.yx.entity.PushMessageVo;
import com.yx.mapper.PushMessageMapper;
import com.yx.service.PushMessageService;


@Service
@Component(value = "pushMessageService")
public class PushMessageServiceImpl  extends ServiceImpl<PushMessageMapper, PushMessage> implements PushMessageService{
	@Autowired
    private PushMessageMapper pushMessageMapper;
	@Override
	public List<PushMessage> getPushMessageByUserId(int receiveUserId,int state) {
		return pushMessageMapper.getPushMessageByUserId(receiveUserId,state);
	}

	@Override
	public void updataPushMessage(int userId, String objId, int type,int state) {
		pushMessageMapper.updataPushMessage(userId, objId, type,state);
	}

	@Override
	public void updataPushMessages(int userId, int state) {
		pushMessageMapper.updataPushMessages(userId,state);
		
	}

	@Override
	public List<CalendarView> getRemindList(String bTime, String eTime) {
		return pushMessageMapper.getRemindList(bTime, eTime);
	}

	@Override
	public List<PushMessageVo> getListGroupObjId(Page<PushMessageVo> page, int receiveUserId, int state) {
		return  pushMessageMapper.getListGroupObjId(page, receiveUserId, state);
	}

	@Override
	public List<PushMessage> getPushMessageByObjId(String objId,String userId,String state) {
		return pushMessageMapper.getPushMessageByObjId(objId,userId,state);
	}

	@Override
	public long msgCount(Integer userId, Integer state) {
		// TODO Auto-generated method stub
		return pushMessageMapper.msgCount(userId, state);
	}

	@Override
	public List<PushMessageVo> listGroup(Page<PushMessageVo> page, int receiveUserId, int state) {
		// TODO Auto-generated method stub
		return pushMessageMapper.getListGroup(page, receiveUserId, state);
	}

}
