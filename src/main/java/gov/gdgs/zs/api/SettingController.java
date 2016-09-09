package gov.gdgs.zs.api;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.YwglService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.entity.ResponseMessage;


/**
 * 注师系统参数API 
 * @author ming
 *
 */

@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class SettingController {
	
	@Resource
	private SettingService settingService;
	
	/**
	 * 获取有效的业务报备
	 */
	@RequestMapping(value = "/setting", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getYwbb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = settingService.getYwbb(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	

	/*
	 * 修改业务报备信息
	 */
	@RequestMapping(value = "/setting/{id}", method = RequestMethod.PUT)
	public  ResponseEntity<?> getYwbb(
			@RequestBody Map<String,Object> map,
			@PathVariable String id){ 
		settingService.updateYwbb(id,map);
		ResponseMessage rm  = new ResponseMessage(ResponseMessage.Type.success, "200", "更新成功");
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	
	


}
