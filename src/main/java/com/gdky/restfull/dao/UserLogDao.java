package com.gdky.restfull.dao;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.entity.User;

@Repository
public class UserLogDao extends BaseJdbcDao {

	public Number addLog(User user, String ip,String time, String action) {
		String sql = "insert into fw_user_log (user_id,ACCESS_IP,ACCESS_TIME,ACTION) values(?,?,?,?)";
		return this.insertAndGetKeyByJdbc(sql, new Object[]{user.getId(),ip,time,action},new String[]{"id"});	
	}

}
