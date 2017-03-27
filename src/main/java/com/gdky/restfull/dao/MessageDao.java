package com.gdky.restfull.dao;

import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.utils.Common;

@Repository
public class MessageDao extends BaseJdbcDao {

	public Map<String, Object> getSendBox(User user,Condition condition, int page,
			int pagesize) {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT t.id,t.create_time,t.title,t.reciver,dm.mc as 'type', u.`NAMES` as sender,");
		sb.append(" @rownum := @rownum + 1 as xh ");
		sb.append(" FROM fw_msg_text t,dm_msg_type dm,fw_users u, (SELECT @rownum:=?) temp, ");

		// <=== 查询条件集合
		sb.append(" ( " + condition.getSelectSql("fw_msg_text", "id"));
		sb.append("    ORDER BY CREATE_TIME desc  ");
		sb.append("    LIMIT ? , ?) sub ");
		// ===> 插入查询条件集合结束

		sb.append(" WHERE t.id = sub.id  ");
		sb.append(" and t.`TYPE` = dm.id  ");
		sb.append(" and t.SENDERID = u.ID  ");

		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(0, startIndex);
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),params.toArray(),
				new RowMapper<Map<String, Object>>() {
			public Map<String, Object> mapRow(ResultSet rs, int arg1)
					throws SQLException {
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"yyyy-MM-dd HH:mm:ss");
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("xh", rs.getObject("xh"));
				map.put("id", rs.getObject("id"));
				map.put("create_time", sdf.format(rs.getTimestamp("create_time")));
				map.put("title", rs.getObject("title"));
				map.put("reciver", rs.getObject("reciver"));
				map.put("sender", rs.getObject("sender"));
				map.put("type", rs.getObject("type"));
				return map;
			}
		});

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

	public List<String> getUsersBySWS() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.id from fw_users u , zs_jg j , fw_user_role ur ");
		sb.append(" where u.JG_ID = j.ID ");
		sb.append(" and u.ID = ur.USER_ID ");
		sb.append(" and j.yxbz = 1 ");
		sb.append(" and ur.ROLE_ID =3 ");
		List<String> ls = this.jdbcTemplate.query(sb.toString(),
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						String str = rs.getString("id");
						return str;
					}
				});
		return ls;
	}

	public void send(User sender, String title, String content, Integer type,
			List<String> recivers, String reciverDes, String exp_time) {

		String uuid_text = Common.newUUID();
		String cre_time = Common.getCurrentTime2MysqlDateTime();

		StringBuffer sb = new StringBuffer();
		// 先添加消息本体
		sb.append(" insert into fw_msg_text ");
		sb.append(" (id,title,content,senderid,reciver,type,create_time,expired_time) ");
		sb.append(" values(?,?,?,?,?,?,?,?) ");
		this.jdbcTemplate.update(sb.toString(), new Object[] { uuid_text,
				title, content, sender.getId(), reciverDes, type, cre_time,
				exp_time });
		// 添加接受人名单
		String uuid_log ;
		sb.setLength(0);
		sb.append(" insert into fw_msg_log ");
		sb.append(" (id,reciid,sendid,textid,zt) ");
		sb.append(" values(?,?,?,?,?) ");
		List<Object[]> batchArgs = new ArrayList<>();
		for (int i = 0; i < recivers.size(); i++) {
			uuid_log = Common.newUUID();
			batchArgs.add(new Object[] { uuid_log, recivers.get(i),
					sender.getId(), uuid_text, 1 });
		}
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);

	}

	public Map<String, Object> getInbox(User user, Condition condition, int page,
			int pagesize) {
		condition.add("t1.reciid", Condition.EQUAL, user.getId());
		StringBuffer sb = new StringBuffer();
		sb.append(" select SQL_CALC_FOUND_ROWS t1.id,t1.textid, t1.zt,t2.title,t2.create_time, ");
		sb.append(" (CASE WHEN t2.type=2 THEN '系统通知' WHEN T2.TYPE = 3 THEN '缴费通知'  ELSE '一般消息' END) as 'type' ");
		sb.append(" from fw_msg_log t1, fw_msg_text t2 ");
		sb.append(condition.getSql());
		sb.append(" and t2.expired_time > now() ");
		sb.append(" and t1.textid = t2.id ");
		sb.append(" and t1.zt != 0 ");
		sb.append(" order by t2.create_time desc ");
		sb.append(" limit ?, ? " );


		// 装嵌传值数组
		int startIndex = pagesize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(startIndex);
		params.add(pagesize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(),new RowMapper<Map<String, Object>>() {
					public Map<String, Object> mapRow(ResultSet rs, int arg1)
							throws SQLException {
						java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", rs.getObject("id"));
						map.put("create_time", sdf.format(rs.getTimestamp("create_time")));
						map.put("title", rs.getObject("title"));
						map.put("zt", rs.getObject("zt"));
						map.put("type", rs.getObject("type"));
						map.put("textid", rs.getObject("textid"));
						return map;
					}
				});

		// 获取符合条件的记录数
		int total = this.jdbcTemplate.queryForObject("SELECT FOUND_ROWS()",
				int.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pagesize", pagesize);
		obj.put("current", page);

		return obj;
	}

	public List<String> getUsersByWSSWS() {
		StringBuffer sb = new StringBuffer();
		sb.append(" select u.id from fw_users u , zs_jg j , fw_user_role ur ");
		sb.append(" where u.JG_ID = j.ID ");
		sb.append(" and u.ID = ur.USER_ID ");
		sb.append(" and j.yxbz = 1 ");
		sb.append(" and ur.ROLE_ID =114 ");
		List<String> ls = this.jdbcTemplate.query(sb.toString(),
				new RowMapper<String>() {
					public String mapRow(ResultSet rs, int arg1)
							throws SQLException {
						String str = rs.getString("id");
						return str;
					}
				});
		return ls;
	}

	public void groupSend(User sender, String title, String content,
			Integer type, List<String> recivers, String reciverDes, String exp_time,Object file) {
		String cre_time = Common.getCurrentTime2MysqlDateTime();
		StringBuffer sb = new StringBuffer();
		// 先添加消息本体
		sb.append(" insert into fw_msg_text ");
		sb.append(" (id,title,content,senderid,role,reciver,type,create_time,expired_time,file) ");
		sb.append(" values(?,?,?,?,?,?,?,?,?,?) ");
		List<Object[]> batchArgs = new ArrayList<>();
		for (int i = 0; i < recivers.size(); i++) {
			String uuid_text = Common.newUUID();
			batchArgs.add(new Object[] {uuid_text, title, content, sender.getId(), recivers.get(i),reciverDes, type, cre_time,
					exp_time,file });
		}
		this.jdbcTemplate.batchUpdate(sb.toString(), batchArgs);

		
	}

	public Map<String, Object> getMsg(String id) {
		String sql = " select title,file,(select a.FILENAME from fw_filename_url a  where a.url=file and a.yxbz=1) as fjName,content,DATE_FORMAT(create_time,'%Y-%m-%d %T') as create_time from fw_msg_text where id = ? ";
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql,new Object[]{id});
		if(ls.size()>0){
			return ls.get(0);
		}
		return null;
	}

	/*
	 * 发送至未缴费会员信息
	 */
	public void sendToWJF(User sender, String title, String content,
			Integer type, String label, String exp_time, String year,Object file) {
		String uuid = Common.newUUID();
		String cre_time = Common.getCurrentTime2MysqlDateTime();
		
		StringBuffer sb = new StringBuffer();
		// 先添加消息本体
		sb.append(" insert into fw_msg_text ");
		sb.append(" (id,title,content,senderid,reciver,type,create_time,expired_time,file) ");
		sb.append(" values(?,?,?,?,?,?,?,?,?) ");
		this.jdbcTemplate.update(sb.toString(), new Object[] { uuid,
				title, content, sender.getId(), label, type, cre_time,
				exp_time,file });
		//再添加接收人记录
		sb.setLength(0);
		sb.append(" insert into fw_msg_log (reciid,sendid,textid,zt) ");
		sb.append(" select t1.id,?,?,? from ( ");
		sb.append(" select distinct u.id from ( ");
		sb.append(" SELECT b.id,	f_qjtt(f_yjtt(b.id,?),b.id,?) AS qjtt,	f_qjgr(( ");
		sb.append(" SELECT COUNT(c.RY_ID)*800 AS yjgr ");
		sb.append(" FROM zs_zysws c ");
		sb.append(" WHERE c.JG_ID=b.id AND c.YXBZ=1 AND c.ry_id NOT IN ( ");
		sb.append(" SELECT d.RY_ID ");
		sb.append(" FROM zs_hyhfjfryls d ");
		sb.append(" WHERE d.nd=?)),b.id,?) AS qjgr	 ");
		sb.append(" FROM zs_jg b ");
		sb.append(" WHERE 1=1 AND b.yxbz=1) g, ");
		sb.append(" fw_users u  ");
		sb.append(" where (g.qjtt>0 || g.qjgr>0) ");
		sb.append(" and g.id = u.JG_ID ");
		sb.append(" ) as t1 ");
		this.jdbcTemplate.update(sb.toString(), new Object[]{sender.getId(),uuid,1,year,year,year,year});
		
	}
	/*
	 * 发送至未交财务报表会员
	 */
	public void sendToWSBCWBB(User sender, String title, String content,
			Integer type, String label, String exp_time, String year,Object file) {
		String uuid = Common.newUUID();
		String cre_time = Common.getCurrentTime2MysqlDateTime();
		
		StringBuffer sb = new StringBuffer();
		// 先添加消息本体
		sb.append(" insert into fw_msg_text ");
		sb.append(" (id,title,content,senderid,reciver,type,create_time,expired_time,file) ");
		sb.append(" values(?,?,?,?,?,?,?,?,?) ");
		this.jdbcTemplate.update(sb.toString(), new Object[] { uuid,
				title, content, sender.getId(), label, type, cre_time,
				exp_time,file });
		//再添加接收人记录
		sb.setLength(0);
		sb.append(" insert into fw_msg_log (reciid,sendid,textid,zt) ");
		sb.append(" select u.id,?,?,? ");
		sb.append(" FROM zs_jg d,fw_users u ");
		sb.append(" where ( d.ID not in( select a.JG_ID from zs_cwbb_lrgd a where a.nd = ? AND a.ZTBJ= 1 ) ");
		sb.append(" or d.ID not in (select a.JG_ID from zs_cwbb_zcfzgd a where a.nd = ? AND a.ZTBJ= 1 ) ");
		sb.append(" or d.ID not in (select a.JG_ID from zs_cwbb_lrfp a where a.nd = ? AND a.ZTBJ = 1) ");
		sb.append(" or d.ID not in (select a.JG_ID from zs_cwbb_xjll a where a.nd = ? AND a.ZTBJ = 1) ");
		sb.append(" or d.ID not in (select a.JG_ID from zs_cwbb_zcmx a where a.nd = ? AND a.ZTBJ = 1) ");
		sb.append(" ) ");
		sb.append(" and d.id = u.jg_id ");
		sb.append(" and d.YXBZ =1 ");
		sb.append(" order by d.id ");
		this.jdbcTemplate.update(sb.toString(), new Object[]{sender.getId(),uuid,1,year,year,year,year,year});
	}

	/*
	 * 发送至未上报报表事务所
	 */
	public void sendToWSBHYBB(User sender, String title, String content,
			Integer type, String label, String exp_time, String year,Object file) {
		String uuid = Common.newUUID();
		String cre_time = Common.getCurrentTime2MysqlDateTime();
		
		StringBuffer sb = new StringBuffer();
		// 先添加消息本体
		sb.append(" insert into fw_msg_text ");
		sb.append(" (id,title,content,senderid,reciver,type,create_time,expired_time,file) ");
		sb.append(" values(?,?,?,?,?,?,?,?) ");
		this.jdbcTemplate.update(sb.toString(), new Object[] { uuid,
				title, content, sender.getId(), label, type, cre_time,
				exp_time,file });
		//再添加接收人记录
		sb.setLength(0);
		sb.append(" insert into fw_msg_log (reciid,sendid,textid,zt) ");
		sb.append(" select u.id,?,?,? ");
		sb.append(" FROM zs_jg d, fw_users u ");
		sb.append(" where ( d.ID not in( SELECT b.jg_id from zs_sdsb_swsjbqk b where  b.nd = ? and b.ZTBJ=2) ");
		sb.append(" or d.ID not in( SELECT b.jg_id from zs_sdsb_hyryqktj b where  b.nd = ? and b.ZTBJ=2) ");
		sb.append(" or d.ID not in( SELECT b.jg_id from zs_sdsb_jysrqk b where  b.nd = ? and b.ZTBJ=2) ");
		sb.append(" or d.ID not in( SELECT b.jg_id from zs_sdsb_jygmtjb b where  b.nd = ? and b.ZTBJ=2) ");
		sb.append(" or d.ID not in( SELECT b.jg_id from zs_sdsb_jzywqktjb b where  b.nd = ? and b.ZTBJ=2) ");
		sb.append(" ) ");
		sb.append(" and d.ID = u.JG_ID ");
		sb.append(" and d.YXBZ =1 ");
		sb.append(" order by d.id ");
		this.jdbcTemplate.update(sb.toString(), new Object[]{sender.getId(),uuid,1,year,year,year,year,year});
		
	}

	public void delMsg(String id) {
		String sql = "delete from fw_msg_log where textid = ?";
		this.jdbcTemplate.update(sql, new Object[]{id});
		sql = "delete from fw_msg_text where id =?";
		this.jdbcTemplate.update(sql, new Object[]{id});
		
	}

	

	public void setRead(String id, String logId) {
		String sql = " update fw_msg_log set zt = 2 where id=? and textid=? ";
		this.jdbcTemplate.update(sql, new Object[]{logId,id});		
	}

	public List<Map<String, Object>> getUserUnread(User user) {
		String sql = "select id from fw_msg_log where reciid = ? and zt = 1 ";
		List<Map<String,Object>> ls = this.jdbcTemplate.queryForList(sql, new Object[]{user.getId()});
		if(ls.size()>0){
			return ls;
		}
		return null;
	}

	public void pullUnreadMessageFromRole(User user, Role role) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into fw_msg_log (textid,sendid,reciid,zt) ");
		sb.append(" select t.ID,t.SENDERID,?,1 ");
		sb.append(" from fw_msg_text t ");
		sb.append(" where t.ROLE = ? ");
		sb.append(" and t.EXPIRED_TIME > now() ");
		sb.append(" and t.ID not in (select textid from fw_msg_log l where l.reciid= ?) ");
		this.jdbcTemplate.update(sb.toString(), new Object[]{user.getId(),role.getId(),user.getId()});
		
	}
}
