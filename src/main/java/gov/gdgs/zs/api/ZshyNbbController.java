package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.ZshyNbbService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 注税行业年报表
 * @author lss
 *
 */
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class ZshyNbbController {
	
	@Resource
	private ZshyNbbService zshyNbbService;
	
	 /**
	  * 事务所基本情况统计表1
	  * @param page
	  * @param pageSize
	  * @param where
	  * @return
	  */
	 @RequestMapping(value = "/zshynbb/swsjbqktj", method = RequestMethod.GET )
	 public ResponseEntity<?> getSwsjbqktj(
				@RequestParam(value = "pagenum", required = true) int page,
				@RequestParam(value = "pagesize", required = true) int pageSize,
				@RequestParam(value = "where", required = false) String where){
		Map<String,Object> map=zshyNbbService.getSwsjbqktj(page, pageSize, where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	 
	 /**
	  * 行业人员情况统计表2
	  * @param page
	  * @param pageSize
	  * @param where
	  * @return
	  */
	 @RequestMapping(value = "/zshynbb/hyryqktjb", method = RequestMethod.GET )
	 public ResponseEntity<?> getHyryqktjb(
				@RequestParam(value = "where", required = false) String where){
		Map<String,Object> map=zshyNbbService.getHyryqktjb(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	} 
	
}
