package com.gdky.restfull.service;

import gov.gdgs.zs.untils.Condition;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.gdky.restfull.dao.MessageDao;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;

@Service
public class MessageService {
	
	@Resource
	private MessageDao messageDao;
	/**
	 * 创建普通消息
	 * 
	 */
	public Map<String,Object> newMsg(User sendUser,  Map<String,Object> message){

		return null;
	}
	
	/**
	 * 创建一条系统消息
	 * @param reciUser 接收人
	 * @param title 消息标题
	 * @param content 消息内容
	 */
	public void newSysMsg(List<User> reciUser, String title, String content){
		
	}
	
	/**
	 * 撤回一条发送的消息
	 * @param sendUser
	 * @param msgId
	 */
	public void withDrawMsg(User sendUser, String msgId){
		
	}
	
	/**
	 * 删除一条接收消息
	 * @param reciUser
	 * @param msgId
	 */
	public void delMsg (User reciUser, String msgId){
		
	}
	
	/**
	 * 获取发件箱列表
	 * @param sendUser
	 * 
	 * @return
	 */
	public Map<String,Object> getSendBox (User sendUser,int page, int pagesize,String whereparam){
		Condition condition = new Condition();
		if(!StringUtils.isEmpty(whereparam)){
			Map<String,Object> where = Common.decodeURItoMap(whereparam);
			condition.add("title", "FUZZY", where.get("title"));
		}
		condition.add("senderid", Condition.EQUAL, sendUser.getId());
		Map<String, Object> obj = messageDao.getSendBox(condition, page, pagesize);
		
		return obj;
	}
	
	/**
	 * 获取收件箱列表
	 * @param reciUser
	 * @return
	 */
	public List<Map<String,Object>> getInBox (User reciUser){
		return null;
	}

}
