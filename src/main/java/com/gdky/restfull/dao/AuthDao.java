package com.gdky.restfull.dao;

import gov.gdgs.zs.untils.Common;
import gov.gdgs.zs.untils.Condition;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.gdky.restfull.entity.Privileges;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.exception.ResourceNotFoundException;
import com.gdky.restfull.utils.HashIdUtil;

@Repository
@Transactional
public class AuthDao extends BaseJdbcDao {

	public List<User> getUser(String userName) {
		String sql = "select * from fw_users where username = ?";
		List<User> ls = this.jdbcTemplate.query(sql, new Object[] { userName },
				new BeanPropertyRowMapper<User>(User.class));
		return ls;
	}

	public List<User> getUserByUname(String uname) {
		String sql = "select * from fw_users where uname = ?";
		List<User> ls = this.jdbcTemplate.query(sql, new Object[] { uname },
				new BeanPropertyRowMapper<User>(User.class));
		return ls;
	}

	public List<Role> getRolesByUser(String userName) {
		StringBuffer sb = new StringBuffer();
		sb.append(" select r.* from fw_users u, fw_user_role ur ,fw_role  r ");
		sb.append(" where u.ID = ur.USER_ID ");
		sb.append(" and ur.ROLE_ID = r.ID ");
		sb.append(" and u.USERNAME =? ");
		List<Role> ls = this.jdbcTemplate.query(sb.toString(),
				new Object[] { userName }, new BeanPropertyRowMapper<Role>(
						Role.class));
		return ls;
	}

	public List<Role> getRoles() {
		String sql = "select * from fw_role t order by t.id desc";
		List<Role> ls = this.jdbcTemplate.query(sql,
				new BeanPropertyRowMapper<Role>(Role.class));
		return ls;
	}

	public List<Privileges> getPrivileges(Integer roleId) {
		String sql = "select * from fw_role_menu where role_id = ?";
		List<Privileges> ls = this.jdbcTemplate.query(sql,
				new Object[] { roleId }, new BeanPropertyRowMapper<Privileges>(
						Privileges.class));
		return ls;

	}

	public void delPrivileges(Integer roleId) {
		String sql = "delete from fw_role_menu where role_id = ?";
		this.jdbcTemplate.update(sql, new Object[] { roleId });

	}

	public Number insertPrivileges(Integer roleId, List<String> privileges) {
		int rs = 0;
		if (privileges.size() > 0) {
			List<Object[]> batchValue = new ArrayList<Object[]>();
			for (String str : privileges) {
				Object[] obj = new Object[] { roleId, str };
				batchValue.add(obj);
			}
			String sql = "insert into fw_role_menu (role_id,menu_id) values (?,?)";
			rs = this.jdbcTemplate.batchUpdate(sql, batchValue).length;
		}

		return rs;
	}

	public List<Map<String, Object>> getPath(List<String> menuIds) {
		Condition condition = new Condition();
		for (int i = 0; i < menuIds.size(); i++) {
			if (i == 0) {
				condition.add("id", Condition.EQUAL, menuIds.get(i));
			}
			condition.or("id", Condition.EQUAL, menuIds.get(i));
		}
		StringBuffer sb = new StringBuffer();
		sb.append(" select path from fw_menu ");
		sb.append(condition.getSql());
		sb.append(" group by path ");

		List<Map<String, Object>> ls = this.jdbcTemplate.queryForList(
				sb.toString(), condition.getParams().toArray());
		return ls;
	}

	public Number addRole(Map<String, Object> obj) {
		String name = (String) obj.get("name");
		String desc = (String) obj.get("description");
		String sql = "insert into fw_role (name,description) values(?,?)";
		return this.insertAndGetKeyByJdbc(sql, new Object[] { name, desc },
				new String[] { "id" });

	}

	public Integer delRole(Integer roleId) {
		String sql = "delete from fw_role where id = ?";
		Integer rs = this.jdbcTemplate.update(sql, new Object[] { roleId });
		return rs;

	}

	public void updateRole(Role role) {
		String sql = "update fw_role set name=?,description=? where id=?";
		this.jdbcTemplate.update(
				sql,
				new Object[] { role.getName(), role.getDescription(),
						role.getId() });

	}

	public Map<String, Object> getUsers(int page, int pageSize,
			HashMap<String, Object> where) {
		Condition condition = new Condition();
		condition.add(" And u.id = ur.user_id ");
		condition.add("ur.role_id", Condition.EQUAL, where.get("roleId"));
		condition.add("u.names", Condition.FUZZY, where.get("names"));
		condition.add("u.username", Condition.FUZZY, where.get("username"));
		condition.add("u.uname", Condition.FUZZY, where.get("uname"));
		condition.add("u.account_enabled", Condition.EQUAL,
				where.get("accountEnabled"));
		condition.add("u.account_expired", Condition.EQUAL,
				where.get("accountExpired"));
		condition.add("u.account_locked", Condition.EQUAL,
				where.get("accountLocked"));
		condition.add("u.credentials_expired", Condition.EQUAL,
				where.get("credentialsExpired"));

		StringBuffer sb = new StringBuffer();
		sb.append("SELECT  ");
		sb.append("    @rownum:=@rownum + 1 AS 'key', u.* ");
		sb.append("FROM ");
		sb.append("    fw_users u,fw_user_role ur, ");
		sb.append("    (SELECT @rownum:=?) n ");
		sb.append(condition.getSql());
		sb.append("    ORDER BY u.CREATE_TIME DESC ");
		sb.append("    LIMIT ? , ? ");

		// 装嵌传值数组
		int startIndex = pageSize * (page - 1);
		ArrayList<Object> params = condition.getParams();
		params.add(0, startIndex);
		params.add(startIndex);
		params.add(pageSize);

		// 获取符合条件的记录
		List<Map<String, Object>> ls = jdbcTemplate.query(sb.toString(),
				params.toArray(), new UserRowMapper());
		String countSql = condition.getCountSql("u.id",
				"fw_users u,fw_user_role ur");
		int total = jdbcTemplate.queryForObject(countSql, condition.getParams()
				.toArray(), Integer.class);

		Map<String, Object> obj = new HashMap<String, Object>();
		obj.put("data", ls);
		obj.put("total", total);
		obj.put("pageSize", pageSize);
		obj.put("current", page);

		return obj;
	}

	public Integer addUser(User u) {
		StringBuffer sb = new StringBuffer();
		sb.append(" insert into fw_users ");
		sb.append(" (version,username,uname,password,password_hint,email,website,");
		sb.append(" address,city,province,country,postal_code,");
		sb.append(" account_enabled,account_expired,account_locked,CREDENTIALS_EXPIRED,");
		sb.append(" JG_ID,IDCARD,NAMES,PHONE,CREATE_TIME) ");
		sb.append(" values(?,?,?,?,?,?,?,  ?,?,?,?,?,  ?,?,?,?,  ?,?,?,?,?) ");
		Object[] param = new Object[] { 0, u.getUsername(), u.getUname(),
				u.getPassword(), u.getPasswordHint(), u.getEmail(), null, null,
				null, null, null, null, 1, 0, 0, 0, u.getJgId(), u.getIdcard(),
				u.getNames(), u.getPhone(), u.getCreateTime() };
		Number userId = this.insertAndGetKeyByJdbc(sb.toString(), param,
				new String[] { "id" });
		return userId.intValue();
	}

	public void updateUser(User u) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update fw_users ");
		sb.append(" set uname=?,jg_id=?,idcard=?,names=?,phone=?,account_enabled=?,account_expired=?,account_locked=?");
		sb.append(" where id = ? ");
		this.jdbcTemplate.update(
				sb.toString(),
				new Object[] { u.getUname(), u.getJgId(),
						u.getIdcard(), u.getNames(), u.getPhone(),
						u.getAccountEnabled(), u.getAccountExpired(),
						u.getAccountLocked(),u.getId() });
	}

	public void addRoleUser(int role, Integer userId) {
		String sql = "insert into fw_user_role (user_id,role_id) values(?,?)";
		this.jdbcTemplate.update(sql, new Object[] { userId, role });

	}

	public void delRoleUser(Integer userId) {
		String sql = "delete from fw_user_role where user_id =?";
		this.jdbcTemplate.update(sql, new Object[] { userId });
	}

	public Integer delUsers(ArrayList<Object[]> batchValue) {
		String sql = "delete from fw_users where id = ?";
		int rs = this.jdbcTemplate.batchUpdate(sql, batchValue).length;
		return rs;

	}

	public Map<String, Object> getUserById(Long id) {
		String sql = "select u.*,j.DWMC,ur.role_id from (fw_users u,fw_user_role ur) left join zs_jg j on u.JG_ID = j.ID where u.id = ur.user_id and u.id = ? ";
		List<Map<String, Object>> ls = this.jdbcTemplate.query(sql,
				new Object[] { id }, new RowMapper<Map<String, Object>>() {
					@Override
					public Map<String, Object> mapRow(ResultSet rs, int rowNum)
							throws SQLException {
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("id", HashIdUtil.encode(rs.getInt("ID")));
						map.put("username", rs.getString("USERNAME"));
						map.put("jgId", HashIdUtil.encode(rs.getInt("JG_ID")));
						map.put("jgMc", rs.getString("DWMC"));
						map.put("names", rs.getString("NAMES"));
						map.put("uname", rs.getString("UNAME"));
						map.put("accountEnabled", rs.getInt("ACCOUNT_ENABLED"));
						map.put("accountExpired", rs.getInt("ACCOUNT_EXPIRED"));
						map.put("accountLocked", rs.getInt("ACCOUNT_LOCKED"));
						map.put("idcard", rs.getString("IDCARD"));
						map.put("phone", rs.getString("PHONE"));
						map.put("createTime", rs.getDate("CREATE_TIME"));
						map.put("roleId", String.valueOf(rs.getInt("role_id")));
						return map;
					}
				});
		if (ls.size() > 0) {
			return ls.get(0);
		} else {
			throw new ResourceNotFoundException(null);
		}
	}
	
	public void resetPass(Integer userId, String password) {
		StringBuffer sb = new StringBuffer();
		sb.append(" update fw_users ");
		sb.append(" set password=? ");
		sb.append(" where id = ? ");
		this.jdbcTemplate.update(
				sb.toString(),
				new Object[] {password,userId});
	}

	public class UserRowMapper implements RowMapper<Map<String, Object>> {

		@Override
		public Map<String, Object> mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", HashIdUtil.encode(rs.getInt("id")));
			map.put("username", rs.getString("username"));
			map.put("jgId", HashIdUtil.encode(rs.getInt("jg_id")));
			map.put("names", rs.getString("names"));
			map.put("uname", rs.getString("uname"));
			map.put("accountEnabled", rs.getInt("account_enabled"));
			map.put("accountExpired", rs.getInt("account_expired"));
			map.put("accountLocked", rs.getInt("account_locked"));
			map.put("credentialsExpired", rs.getInt("credentials_expired"));
			map.put("idcard", rs.getString("idcard"));
			map.put("phone", rs.getString("phone"));
			map.put("createTime", rs.getDate("create_time"));

			return map;
		}

	}

	


}
