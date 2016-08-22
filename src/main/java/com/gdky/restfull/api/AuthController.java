package com.gdky.restfull.api;


import java.util.List;
import java.util.Map;

import javax.xml.ws.soap.AddressingFeature.Responses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.AsideMenu;
import com.gdky.restfull.entity.AuthRequest;
import com.gdky.restfull.entity.AuthResponse;
import com.gdky.restfull.entity.Privileges;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.security.CustomUserDetails;
import com.gdky.restfull.security.TokenUtils;
import com.gdky.restfull.service.AccountService;
import com.gdky.restfull.service.AuthService;
import com.gdky.restfull.utils.HashIdUtil;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX)
public class AuthController {
	
	  @Autowired
	  private AuthenticationManager authenticationManager;

	  @Autowired
	  private TokenUtils tokenUtils;

	  @Autowired
	  private UserDetailsService userDetailsService;
	  @Autowired
	  private AccountService accountService;
	  
	  @Autowired
	  private AuthService authService;
	  
	
	/**
	 * 身份认证接口，使用jwt验证，以post方式提交{"username":"<name>","password":"<password>"}
	 * 成功后获取一个hash过的token
	 * {"token" : "<token hasn>"}
	 * 访问验证api时，在请求头部加上 x-auth-token: <token hasn>
	 * 测试验证api /protect/api
	 * @throws AuthenticationException
	 */
	@RequestMapping(value = "/auth", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody AuthRequest authReq) throws AuthenticationException{
		//进行验证
	    Authentication authentication = this.authenticationManager.authenticate(
	      new UsernamePasswordAuthenticationToken(
	        authReq.getUsername(),
	        authReq.getPassword()
	      )
	    );
	    
	    SecurityContextHolder.getContext().setAuthentication(authentication);

	    // Reload password post-authentication so we can generate token
	    CustomUserDetails userDetails = (CustomUserDetails) this.userDetailsService.loadUserByUsername(authReq.getUsername());
	    String token = this.tokenUtils.generateToken(userDetails);
	    
	    AuthResponse resp = new AuthResponse(token);
	    resp.setTokenhash(token);
	    resp.setJgId(userDetails.getJgId());
	    resp.setPermission(accountService.getPermissionByUser(userDetails));

	    // 返回 token与账户信息
	    return ResponseEntity.ok(resp);
	}
	
	@RequestMapping(value = "/auth", method = RequestMethod.GET)
	public ResponseEntity<?> validateAuth() {
		return ResponseEntity.ok("ok");
	}
	
	@RequestMapping(value="/roles",method = RequestMethod.GET)
	public ResponseEntity<?> getRoles(){
		List<Role> ls = authService.getRoles();
		return ResponseEntity.ok(ls);
	}
	@RequestMapping(value="/roles",method=RequestMethod.POST)
	public ResponseEntity<?> AddRole(@RequestBody Map<String,Object> obj){
		authService.addRole(obj);
		return new ResponseEntity<>(ResponseMessage.success("添加成功"),HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/roles/{roleId}", method=RequestMethod.DELETE)
	public ResponseEntity<?> delRole(@PathVariable Integer roleId){
		authService.delRole(roleId);
		return  ResponseEntity.ok(null);
	}
	
	@RequestMapping(value="/roles", method=RequestMethod.PUT)
	public ResponseEntity<?> updateRole(@RequestBody Role role){
		authService.updateRole(role);
		return  ResponseEntity.ok(null);
	}
	
	@RequestMapping(value="/privileges/{roleId}",method=RequestMethod.GET)
	public ResponseEntity<?> getPrivileges(@PathVariable Integer roleId){
		List<Privileges> ls = authService.getPrivileges(roleId);
		return ResponseEntity.ok(ls);
	}
	
	@RequestMapping(value="/privileges",method=RequestMethod.PUT)
	public ResponseEntity<?> updatePrivileges(@RequestBody Map<String,Object> obj){
		List<String> privileges = (List<String>) obj.get("privileges");
		Integer roleId = (Integer) obj.get("roleId");
		
		authService.delPrivileges(roleId);
		if (privileges.size()>0){
			authService.insertPrivileges(roleId,privileges);
		}		
		return ResponseEntity.ok(null);
	}
	/*
	 * 获取用户列表
	 */
	@RequestMapping(value="/users",method=RequestMethod.GET)
	public ResponseEntity<?> getUsers(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where){
		Map<String,Object> rs = authService.getUsers(page, pageSize, where);
		
		return ResponseEntity.ok(rs);
	}
	
	/*
	 * 获取用户明细
	 */
	@RequestMapping(value="/users/{hashid}",method=RequestMethod.GET)
	public ResponseEntity<?> getUsers(
			@PathVariable String hashid){
		Map<String,Object> rs = authService.getUsersById(hashid);		
		return ResponseEntity.ok(rs);
	}
	
	/*
	 * 添加新用户
	 * 用户信息中需带有roleId属性，标识用户所属角色
	 * 用户信息中需带有jgId属性，标识用户所属事务所，如非事务所用户，jgId=null
	 */
	@RequestMapping(value="/users",method=RequestMethod.POST)
	public ResponseEntity<?> addUser(
			@RequestBody Map<String,Object> user){
		int role = Integer.parseInt((String)user.get("roleId"));
		if(user.get("jgId")!=null){
			String hashId = (String)user.get("jgId");
			Long jgId = HashIdUtil.decode(hashId);
			user.put("jgId", jgId.intValue());	
		}
		Integer userId = authService.addUsers(user);
		authService.addRoleUser(role,userId);
		
		return ResponseEntity.ok(ResponseMessage.success("添加成功"));
	}
	
	/*
	 * 修改用户信息
	 * 用户信息中需带有roleId属性，标识用户所属角色
	 * 用户信息中需带有jgId属性，标识用户所属事务所，如非事务所用户，jgId=null
	 */
	@RequestMapping(value="/users/{id}",method=RequestMethod.PUT)
	public ResponseEntity<?> updateUser(
			@PathVariable String id,
			@RequestBody Map<String,Object> user){
		int role = Integer.parseInt((String)user.get("roleId"));
		if(user.get("jgId")!=null){
			String hashId = (String)user.get("jgId");
			Long jgId = HashIdUtil.decode(hashId);
			user.put("jgId", jgId.intValue());	
		}
		user.put("id",id);
		Integer userId = authService.updateUsers(user);
		authService.delRoleUser(userId);
		authService.addRoleUser(role, userId);
		
		return ResponseEntity.ok(ResponseMessage.success("添加成功"));
	}
	
	/**
	 * 删除选中的用户
	 * @param RequestBody List<String> [ID1,ID2,...] 
	 * @method DELETE
	 */
	@RequestMapping(value="/users",method=RequestMethod.DELETE)
	public ResponseEntity<?> delUsers(@RequestBody List<String> userIds){
		int effectRows = authService.delUsers(userIds);
		ResponseMessage rm = new ResponseMessage(ResponseMessage.Type.success, "200", "成功删除了 "+effectRows+" 条记录。");
		return  ResponseEntity.ok(rm);
	}
	
	/**
	 * 重置密码
	 * @param newPass
	 * @return
	 */
	@RequestMapping(value="/password/{userId}",method=RequestMethod.PUT)
	public ResponseEntity<?> resetPass(@RequestBody Map<String,Object> newPass,@PathVariable String userId){
		authService.resetPass(userId,newPass);
		ResponseMessage rm = new ResponseMessage(ResponseMessage.Type.success, "200", "重置密码成功");
		return  ResponseEntity.ok(rm);
	}
	
	
	

}
