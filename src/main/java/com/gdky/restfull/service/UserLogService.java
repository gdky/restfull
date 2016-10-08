package com.gdky.restfull.service;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gdky.restfull.dao.UserLogDao;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;

@Transactional
@Service
public class UserLogService {
	
	@Resource
	private UserLogDao userLogDao;
	
	public void addLog(User user, String ip, String action) {
		String time = Common.getCurrentTime2MysqlDateTime();
		Number id = userLogDao.addLog(user,ip,time,action);
	}
}
