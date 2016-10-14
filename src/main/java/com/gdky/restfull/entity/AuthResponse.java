package com.gdky.restfull.entity;

import java.io.Serializable;
import java.util.List;

import org.hashids.Hashids;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.utils.HashIdUtil;

public class AuthResponse implements Serializable {

	private static final long serialVersionUID = 2652559529529474758L;
	private String token;
	private String tokenhash;
	private String jgId;
	private String permission;
	private Integer lo; //角色代码
	private List<AsideMenu> menu;
	private String names;

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public String getJgId() {
		return jgId;
	}

	public void setJgId(Integer jgId) {
		this.jgId = null;
		if (jgId != null) {
			this.jgId = HashIdUtil.encode(jgId.longValue());
		}
	}

	public String getTokenhash() {
		return tokenhash;
	}

	public void setTokenhash(String token) {
		Md5PasswordEncoder encoder = new Md5PasswordEncoder();
		// MD5不加盐hash
		String last = token.substring(token.length() - 1);
		String pass = encoder.encodePassword(last + token, null);
		this.tokenhash = pass;
	}

	public AuthResponse() {
		super();
	}

	public AuthResponse(String token) {
		this.setToken(token);
	}

	public String getToken() {
		return this.token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getLo() {
		return lo;
	}

	public void setLo(Integer lo) {
		this.lo = lo;
	}

	public List<AsideMenu> getMenu() {
		return menu;
	}

	public void setMenu(List<AsideMenu> menu) {
		this.menu = menu;
	}

	public String getNames() {
		return names;
	}

	public void setNames(String names) {
		this.names = names;
	}
}
