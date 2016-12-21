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
	 * @sender 发送人
	 * @message {
	 * String title, 消息标题
	 * String content, 消息内容
	 * int type, 消息类型 1:系统消息；2:普通消息；3.欠费通知
	 * Boolean groupsend 是否按组发送
	 * Object reciver 接收人
	 * 组发送为true时，接收人格式{key,value}
	 */
	public Map<String, Object> newMsg(User sender, Map<String, Object> message) {
		if (message.get("groupsend") != null
				&& (Boolean) message.get("groupsend")) {
			Map<String,Object> reciver = (Map<String,Object>)message.get("reciver");
			String key = (String)reciver.get("key");
			String title = (String) message.get("title");
			String content = (String) message.get("content");
			Integer type = (Integer) message.get("type");
					
			if("1".equals(key)){
				List<String> recivers = messageDao.getUsersBySWS();
				messageDao.send(sender,title,content,type,recivers,"2");
			}

		} else {

		}

		return null;
	}

	/**
	 * 创建一条系统消息
	 * 
	 * @param reciUser
	 *            接收人
	 * @param title
	 *            消息标题
	 * @param content
	 *            消息内容
	 */
	public void newSysMsg(List<User> reciUser, String title, String content) {

	}

	/**
	 * 撤回一条发送的消息
	 * 
	 * @param sendUser
	 * @param msgId
	 */
	public void withDrawMsg(User sendUser, String msgId) {

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
			condition.add("title", "FUZZY", where.get("title"));
		}
		condition.add("senderid", Condition.EQUAL, sendUser.getId());
		Map<String, Object> obj = messageDao.getSendBox(condition, page,
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
			condition.add("title", "FUZZY", where.get("title"));
		}
		condition.add("reciid", Condition.EQUAL, user.getId());
		Map<String, Object> obj = messageDao.getInbox(condition, page,
				pagesize);
		return obj;
	}

}
