package com.gdky.restfull.api;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.AsideMenu;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;
import com.gdky.restfull.service.AuthService;
import com.gdky.restfull.service.MessageService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX)
public class AccountController {
	
	@Resource
	AccountService accountService;
	
	@Resource
	AuthService userService;
	
	@Autowired
	MessageService messageService;

	/**
	 * 获取用户登录基本信息
	 * name-用户名称，menu-用户菜单，newMsg-新信息提示
	 * @para
	 *
	 */
	@RequestMapping(value="/account")
	public ResponseEntity<?> getAccount(HttpServletRequest request){
		
		User user =  accountService.getUserFromHeaderToken(request);
		
		//获取功能菜单
		List<AsideMenu> menu = accountService.getMenuByUser(user.getId());
		/*//获取模块访问权限
		StringBuffer permission = new StringBuffer();
		for (int i = 0;i<menu.size();i++){
			AsideMenu item = menu.get(i);
			if (item.getHref()!=null && !item.getHref().equals("")){
				permission.append(item.getHref()).append(",");
			}			
		}*/
		//获取新信息
		Map<String,Object> ms = messageService.getMessageShortcut(user);
		
		HashMap<String,Object> resp = new HashMap<String,Object>();
		resp.put("lo", userService.getRolesByUser(user.getUsername()).get(0).getId());
		resp.put("names", user.getNames());
		resp.put("menu", menu);
		resp.put("unread", ms.get("unread"));
		return  ResponseEntity.ok(resp);
	}
}
