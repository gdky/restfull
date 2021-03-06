package com.gdky.restfull.service;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.management.RuntimeErrorException;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.dao.AuthDao;
import com.gdky.restfull.entity.Privileges;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.exception.UserException;
import com.gdky.restfull.security.TokenUtils;
import com.gdky.restfull.utils.HashIdUtil;

@Service
@Transactional
public class AuthService {

	@Resource
	private AuthDao authDao;

	public User getUser(String userName) {
		List<User> ls = authDao.getUser(userName);
		if (ls.size() != 1) {
			return null;
		}
		return ls.get(0);
	}

	public User getUserByUname(String uname) {
		List<User> ls = authDao.getUserByUname(uname);
		if (ls.size() != 1) {
			return null;
		}
		return ls.get(0);
	}

	public List<Role> getRolesByUser(String userName) {
		return authDao.getRolesByUser(userName);
	}

	public List<Role> getRoles() {
		return authDao.getRoles();
	}

	public List<Privileges> getPrivileges(Integer roleId) {
		return authDao.getPrivileges(roleId);
	}

	public void delPrivileges(Integer roleId) {
		authDao.delPrivileges(roleId);
	}

	public void insertPrivileges(Integer roleId, List<String> privileges) {
		List<Map<String, Object>> rows = authDao.getPath(privileges);
		String[] nodes = new String[0];
		for (Map<String, Object> row : rows) {
			String path = (String) row.get("path");
			nodes = ArrayUtils.addAll(nodes, path.split("-"));
		}
		List<String> list = Arrays.asList(nodes);
		list = removeZero(list);
		privileges.addAll(list);
		privileges = unique(privileges);

		Number rs = authDao.insertPrivileges(roleId, privileges);

	}

	public static List<String> unique(List<String> list) {
		// List_unique
		HashSet<String> set = new HashSet<String>(list);
		list.clear();
		list.addAll(set);
		return list;
	}

	public static List<String> removeZero(List<String> items) {
		List<String> list = new ArrayList<String>();
		for (String str : items) {
			if (!str.equals("000")) {
				str = str.replaceFirst("^0*", "");
				list.add(str);
			}
		}
		return list;
	}

	public Number addRole(Map<String, Object> obj) {
		return authDao.addRole(obj);
	}

	public void delRole(Integer roleId) {
		authDao.delPrivileges(roleId);
		authDao.delRole(roleId);
	}

	public void updateRole(Role role) {
		authDao.updateRole(role);
	}

	public Map<String, Object> getUsers(int page, int pageSize, String where) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		if (where != null) {
			try {
				where = java.net.URLDecoder.decode(where, "UTF-8");
				ObjectMapper mapper = new ObjectMapper();
				map = mapper.readValue(where,
						new TypeReference<Map<String, Object>>() {
						});
			} catch (Exception e) {
			}
		}
		Map<String, Object> rs = authDao.getUsers(page, pageSize, map);
		return rs;
	}

	public Integer addUsers(Map<String, Object> obj) {
		User u = new User();
		String password1 = (String) obj.get("password1");
		String password2 = (String) obj.get("password2");
		String username = (String) obj.get("userName");//用户名
		String names = (String) obj.get("names");//姓名
		String uname = (String) obj.get("uname");//登录名
		String idcard = (String) obj.get("idcard");
		String phone = (String) obj.get("phone");

		if (password1 == null || password1.isEmpty() || password2 == null
				|| password2.isEmpty()) {
			throw new UserException("未输入密码");
		}
		if (!password1.equals(password2) ) {
			throw new UserException("两次输入的密码必须一致");
		}
		if (authDao.getUser(username).size() > 0) {
			throw new UserException("用户名已存在，重新输入用户名");
		}
		if (authDao.getUserByUname(uname).size() > 0) {
			throw new UserException("登录名已存在，重新输入登录名");
		}
		if (names == null || names.isEmpty()) {
			throw new UserException("未输入账户描述信息");
		}
		if (phone == null || phone.isEmpty()) {
			throw new UserException("未输入联系电话");
		}
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		u.setCreateTime(new Timestamp(System.currentTimeMillis()));
		u.setPassword(encoder.encodePassword(password1, null));
		u.setJgId((Integer) obj.get("jgId"));
		u.setUname(uname);
		u.setUsername(username);
		u.setNames(names);
		u.setPhone(phone);
		u.setIdcard(idcard);
		Integer userId = authDao.addUser(u);

		return userId;
	}

	public void addRoleUser(int role, Integer userId) {
		authDao.addRoleUser(role,userId);
	}

	public int delUsers(List<String> userIds) {
		ArrayList<Object[]> batchValue = new ArrayList<Object[]>();
		for (String userId : userIds) {
			Long id = HashIdUtil.decode(userId);
			Object[] obj = new Object[] { id };
			batchValue.add(obj);
		}
		int effectRows = authDao.delUsers(batchValue);
		return effectRows;
	}

	public Map<String, Object> getUsersById(String hashid) {
		Long Id = HashIdUtil.decode(hashid);
		Map<String,Object> rs = authDao.getUserById(Id);
		return rs;
	}

	public Integer updateUsers(Map<String, Object> obj) {
		User u = new User();
		Long userId = HashIdUtil.decode((String)obj.get("id"));
		String names = (String) obj.get("names");
		String uname = (String) obj.get("uname");//登录名
		String idcard = (String) obj.get("idcard");
		String phone = (String) obj.get("phone");
		Integer unameChange = (Integer) obj.get("unameChange");
		Integer accountEnabled = (Integer) obj.get("accountEnabled");
		Integer accountExpired = (Integer) obj.get("accountExpired");
		Integer accountLocked = (Integer) obj.get("accountLocked");


		if ( unameChange!= null && authDao.getUserByUname(uname).size() > 0) {
			throw new UserException("登录名已存在，重新输入登录名");
		}
		if (names == null || names.isEmpty()) {
			throw new UserException("未输入账户描述信息");
		}
		if (phone == null || phone.isEmpty()) {
			throw new UserException("未输入联系电话");
		}
		u.setJgId((Integer) obj.get("jgId"));
		u.setUname(uname);
		u.setNames(names);
		u.setPhone(phone);
		u.setIdcard(idcard);
		u.setAccountEnabled(accountEnabled);
		u.setAccountExpired(accountExpired);
		u.setAccountLocked(accountLocked);
		u.setId(userId.intValue());
		
		authDao.updateUser(u);

		return userId.intValue();
	}

	public void delRoleUser(Integer userId) {
		authDao.delRoleUser(userId);
	}

	public void resetPass(String userId,Map<String, Object> newPass) {
		Long id = HashIdUtil.decode(userId);
		String password = (String) newPass.get("password1");
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		password = encoder.encodePassword(password, null);
		authDao.resetPass(id.intValue(),password);
	}

	public void updatePass(User user, Map<String, Object> passGroup) {
		ShaPasswordEncoder encoder = new ShaPasswordEncoder();
		String password = (String) passGroup.get("password");
		String password1 = (String) passGroup.get("password1");
		String password2 = (String) passGroup.get("password2");
		password = encoder.encodePassword(password, null);
		if(!password.equals(user.getPassword())){
			throw new UserException("密码错误");
		}
		if (password1 == null || password1.isEmpty() || password2 == null
				|| password2.isEmpty()) {
			throw new UserException("未输入密码");
		}
		if (!password1.equals(password2) ) {
			throw new UserException("两次输入的密码必须一致");
		}
		password1 = encoder.encodePassword(password1, null);
		authDao.updatePass(password1,user.getId());
	}
	
    public String getForWAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-FORWARDED-FOR");  
        if(ip == null ) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    } 
    public String getRealAddr(HttpServletRequest request) {  
        String ip = request.getHeader("X-Real-IP");  
        if(ip == null ) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
}
