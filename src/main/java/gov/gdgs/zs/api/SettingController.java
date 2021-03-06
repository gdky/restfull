package gov.gdgs.zs.api;


import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SettingService;
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
	 * 获取业务参数
	 */
	@RequestMapping(value = "/ywsettings", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getSettings(){ 
		Map<String,Object> obj = settingService.getSettings("yw",0,100);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	

	/*
	 * 修改业务报备信息
	 */
	@RequestMapping(value = "/settings/{id}", method = RequestMethod.PUT)
	public  ResponseEntity<?> updateSetting(
			@RequestBody Map<String,Object> map,
			@PathVariable String id){ 
		settingService.updateSetting(id,map);
		ResponseMessage rm  = new ResponseMessage(ResponseMessage.Type.success, "200", "更新成功");
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	
	


}
