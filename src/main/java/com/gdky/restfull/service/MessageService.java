package com.gdky.restfull.service;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.dao.AuthDao;
import com.gdky.restfull.dao.MessageDao;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;

@Service
@Transactional
public class MessageService {

	@Resource
	private MessageDao messageDao;
	
	@Resource
	private AuthDao authDao;

	/**
	 * 群组式发送一条新信息
	 * @sender 发送人
	 * @message {
	 * String title, 消息标题
	 * String content, 消息内容
	 * int type, 消息类型 1:一般消息；2:系统消息；3.欠费通知
	 * Object reciver 接收人格式{key,value}
	 * }
	 */
	public Map<String, Object> newGroupMsg(User sender, Map<String, Object> message) {
		Map<String,Object> reciver = (Map<String,Object>)message.get("reciver");
		String key = (String)reciver.get("key");
		String label = (String)reciver.get("label");
		String year =(String)message.get("year");
		String title = (String) message.get("title");
		String content = (String) message.get("content");
		Object file =  message.get("file").toString().equals("false")?null:message.get("file").toString();
		Integer type = (Integer) message.get("type");
		//群组发送的消息设置180天的有效期
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, Constants.MESSAGE_VALID_DAYRANGE);
		String exp_time = Common.getDate2MysqlDateTime(cal.getTime());
		
		//接收对象
		List<String> recivers = new ArrayList<String>();
		if("3".equals(key)){ 
			//1表示省内事务所
			recivers.add("3");
			messageDao.groupSend(sender,title,content,type,recivers,label,exp_time,file);
		
		}else if ("114".equals(key)){
			//114表示外省事务所
			recivers.add("114");
			messageDao.groupSend(sender,title,content,type,recivers,label,exp_time,file);
			
		}else if ("211".equals(key)){
			String reciverDes = year + label;
			messageDao.sendToWJF(sender,title,content,type,reciverDes,exp_time,year,file);
		}else if ("212".equals(key)){
			messageDao.sendToWSBCWBB(sender,title,content,type,label,exp_time,year,file);
		}else if ("213".equals(key)){
			messageDao.sendToWSBHYBB(sender,title,content,type,label,exp_time,year,file);
		}
		return null;
	}
	/**
	 * 向特定机构发送一条新消息
	 * @param sender
	 * @param message
	 * @return
	 */
	public Map<String, Object> newSWSMsg(User sender, Map<String, Object> message) {
		// TODO Auto-generated method stub
		return null;
	}


	/**
	 * 删除一条接收消息
	 * 
	 * @param reciUser
	 * @param msgId
	 */
	public void delMsg(User reciUser, String msgId) {

	}

	/**
	 * 获取发件箱列表
	 * 
	 * @param sendUser
	 * 
	 * @return
	 */
	public Map<String, Object> getSendBox(User sendUser, int page,
			int pagesize, String whereparam) {
		Condition condition = new Condition();
		if (!StringUtils.isEmpty(whereparam)) {
			Map<String, Object> where = Common.decodeURItoMap(whereparam);
			condition.add("title", Condition.FUZZY, where.get("title"));
			condition.add("type", Condition.EQUAL, where.get("type"));
		}
		//condition.add("senderid", Condition.EQUAL, sendUser.getId());
		Map<String, Object> obj = messageDao.getSendBox(sendUser,condition, page,
				pagesize);

		return obj;
	}

	/**
	 * 获取收件箱列表
	 * 	
	 */
	public Map<String, Object> getInBox(User user, int page, int pagesize,
			String whereparam) {
		Condition condition = new Condition();
		if (!StringUtils.isEmpty(whereparam)) {
			Map<String, Object> where = Common.decodeURItoMap(whereparam);
			condition.add("t2.title", "FUZZY", where.get("title"));
		}
		
		Map<String, Object> obj = messageDao.getInbox(user, condition, page,
				pagesize); 
		return obj;
	}

	public Map<String, Object> getMsg(String id, String logId) {
		if(!StringUtils.isEmpty(logId)){
			messageDao.setRead(id,logId);
		}
		return messageDao.getMsg(id);
	}


	/**
	 * 撤回一条发送的消息
	 * 
	 * @param List<String> message
	 */
	public void delMsg(List<String> message) {
		Iterator<String> iter = message.iterator();
		while(iter.hasNext()){
			String id = iter.next();
			messageDao.delMsg(id);			
		}
	}


	/**
	 * 获取用户短信息快照，包括未读信息状态和前5条短信,如有新的用户组群发消息，在log表添加相应的未读记录。
	 * @param user
	 * @return
	 */
	public Map<String, Object> getMessageShortcut(User user) {
		HashMap<String,Object> rs = new HashMap<String,Object>();
		//首先检查新的群发消息
		//拉取角色群发消息
		this.pullUnreadMessageFromRole(user); 
		
		//获取未读状态
		rs.put("unread", this.getUserUnreadStatus(user));
		//获取前5条用户短信
		//rs.put("inbox", this.getUserInboxShort(user));
		return rs;
	}


	private void pullUnreadMessageFromRole(User user) {
		List<Role> role = authDao.getRolesByUser(user.getUsername());
		messageDao.pullUnreadMessageFromRole(user,role.get(0));
	}
	
	private List<Map<String,Object>> getUserInboxShort(User user) {
		Map<String,Object> rs = this.getInBox(user, 1, 5, "");
		List<Map<String,Object>> ls = (List<Map<String,Object>>)rs.get("data");
		return ls;
	}


	private Number getUserUnreadStatus(User user) {
		List<Map<String,Object>> ls = messageDao.getUserUnread(user);
		if(ls != null){
			return ls.size();
		}
		return 0;
	}






	

}
