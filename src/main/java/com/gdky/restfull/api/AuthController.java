package com.gdky.restfull.api;


import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.AuthRequest;
import com.gdky.restfull.entity.AuthResponse;
import com.gdky.restfull.entity.Privileges;
import com.gdky.restfull.entity.Role;
import com.gdky.restfull.security.CustomUserDetails;
import com.gdky.restfull.security.TokenUtils;
import com.gdky.restfull.service.AccountService;
import com.gdky.restfull.service.AuthService;

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
	
	@RequestMapping(value="/privileges/{roleId}",method=RequestMethod.GET)
	public ResponseEntity<?> getPrivileges(@PathVariable Integer roleId){
		List<Privileges> ls = authService.getPrivileges(roleId);
		return ResponseEntity.ok(ls);
	}
	
	@RequestMapping(value="/privileges",method=RequestMethod.PUT)
	public ResponseEntity<?> updatePrivileges(@RequestBody List<String> privileges){
		System.out.println(privileges);
		
		return ResponseEntity.ok(null);
	}
	

}
