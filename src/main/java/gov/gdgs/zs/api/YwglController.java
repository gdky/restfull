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
 * 业务管理API controller
 * @author ming
 *
 */

@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class YwglController {
	
	@Resource
	private YwglService ywglService;
	
	/**
	 * 获取有效的业务报备
	 */
	@RequestMapping(value = "/ywbb", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getYwbb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = ywglService.getYwbb(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	/**
	 * 获取收费金额预警的业务报备列表
	 */
	@RequestMapping(value = "/ywbbsfjeyj", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getYwbbSFJEYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = ywglService.getYwbbSFJEYJ(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	/**
	 * 获取年度不同预警的业务报备列表
	 */
	@RequestMapping(value = "/ywbbndbtyj", method = RequestMethod.GET)
	public  ResponseEntity<?> getYwbbNDBTYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = ywglService.getYwbbNDBTYJ(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	/**
	 * 获取业务委托方变更预警列表
	 */
	@RequestMapping(value = "/ywbbwtfyj", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getYwbbWTFYJ(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = ywglService.getYwbbWTFYJ(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}

	/*
	 * 修改业务报备信息
	 */
	@RequestMapping(value = "/ywbb/{id}", method = RequestMethod.PUT)
	public  ResponseEntity<?> getYwbb(
			@RequestBody Map<String,Object> map,
			@PathVariable String id){ 
		ywglService.updateYwbb(id,map);
		ResponseMessage rm  = new ResponseMessage(ResponseMessage.Type.success, "200", "更新成功");
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	
	/*
	 * 获取业务报备细节信息
	 */
	@RequestMapping(value = "/ywbb/{hash}", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getYwbbById(@PathVariable("hash") String hash){ 
		
		Map<String,Object> obj = ywglService.getYwbbById(hash);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	
	/*
	 * 客户端用业务报备查询
	 */
	@RequestMapping(value="/jg/{hashId}/yw",method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbByJg(
			@PathVariable("hashId") String hashId,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value="where", required=false) String where){
		Map<String,Object> obj = ywglService.getYwbbByJg(hashId,page,pageSize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	/*
	 * 客户端用机构关联执业税务师和机构信息
	 */
	@RequestMapping(value="/ywbbmisc/{jgHashid}",method = RequestMethod.GET)
	public ResponseEntity<?> getYwbbMiscByJg(
			@PathVariable("jgHashid") String jgHashid){
		Map<String,Object> obj = ywglService.getYwbbMiscByJg(jgHashid);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	/*
	 * 客户端提交业务报备信息
	 */
	@RequestMapping(value = "/ywbb", method = RequestMethod.POST)
	public  ResponseEntity<Map<String,Object>> addYwbb(
			@RequestBody Map<String,Object> values){ 

		Map<String,Object> obj = ywglService.addYwbb(values);
		return new ResponseEntity<>(obj,HttpStatus.CREATED);
	}


}
