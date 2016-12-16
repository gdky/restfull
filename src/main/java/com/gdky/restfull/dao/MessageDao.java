package com.gdky.restfull.dao;

import gov.gdgs.zs.untils.Condition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.gdky.restfull.entity.User;

@Repository
public class MessageDao extends BaseJdbcDao {

	public Map<String, Object> getSendBox(Condition condition, int page,
			int pagesize) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT t.id,t.title,t.content,t.reciver, ");
		sb.append(" @rownum := @rownum + 1 as xh ");
		sb.append(" FROM fw_msg_text t, fw_msg_log l, (SELECT @rownum:=?) temp, ");

		// <=== 查询条件集合
		sb.append(" ( " + condition.getSelectSql("fw_msg_text", "id"));
		sb.append("    ORDER BY CREATE_TIME desc  ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束

		sb.append(" WHERE t.id = sub.id  ");
		sb.append(" AND t.id = l.textid  ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.queryForList(sb.toString(),
				params.toArray());

		// 获取符合条件的记录数
		String countSql = condition.getCountSql("id", "fw_msg_text");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public List<Map<String, Object>> getUsersBySWS() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.id from fw_users u , zs_jg j , fw_user_role ur ");
		sb.append(" where u.JG_ID = j.ID ");
		sb.append(" and u.ID = ur.USER_ID ");
		sb.append(" and j.yxbz = 1 ");
		sb.append(" and ur.ROLE_ID =3 ");
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sb.toString());
		return ls;
	}

	public void send(User sender, String title, String content, Integer type) {
		// TODO Auto-generated method stub
		
	}
}
