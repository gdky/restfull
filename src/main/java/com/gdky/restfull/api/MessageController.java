package com.gdky.restfull.api;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;
import com.gdky.restfull.service.MessageService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX)
public class MessageController {

	@Autowired
	private AccountService accountService;
	
	@Autowired
	private MessageService messageService;
	
	@Autowired
	private HttpServletRequest request;

	/**
	 * 获取发件箱列表
	 */
	@RequestMapping(value = "/sendbox", method = RequestMethod.GET)
	public ResponseEntity<?> getSendbox(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where) {

		User user = accountService.getUserFromHeaderToken(request);
		Map<String, Object> obj = messageService.getSendBox(user,page, pagesize, where);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	/**
	 * 发送新信息
	 */
	@RequestMapping(value = "/messages", method = RequestMethod.POST)
	public ResponseEntity<?> newMessages(
			@RequestBody Map<String,Object> message) {
		User user = accountService.getUserFromHeaderToken(request);
		Map<String, Object> obj = messageService.newMsg(user, message);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	/**
	 * 获取消息内容
	 */
	@RequestMapping(value = "/messages/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMessages(
			@PathVariable String id) {
		Map<String, Object> obj = messageService.getMsg( id);
		return new ResponseEntity<>(obj, HttpStatus.OK);
	}
	
	/**
	 * 获取收件箱列表
	 */
	@RequestMapping(value = "/inbox", method = RequestMethod.GET)
	public ResponseEntity<?> getInbox(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value = "where", required = false) String where){
		User user = accountService.getUserFromHeaderToken(request);
		Map<String,Object> obj = messageService.getInBox(user,page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
}
