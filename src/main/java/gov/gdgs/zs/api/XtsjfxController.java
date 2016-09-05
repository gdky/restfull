package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;



import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.XtsjfxService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class XtsjfxController {
	
	@Resource
	private XtsjfxService xtsjfxService;
	
	/*
	 * 机构年检数据分析
	 */
	
	 @RequestMapping(value = "/jgnjsjfxb", method = RequestMethod.GET) 
	 	public  ResponseEntity<Map<String,Object>> getJgnjsjfxb( 
	 			@RequestParam(value = "page", required = true) int page, 
	 			@RequestParam(value = "pageSize", required = true) int pageSize, 
	 			@RequestParam(value="where", required=false) String where){ 
	 		return new ResponseEntity<>(xtsjfxService.getJgnjsjfxb(page, pageSize, where),HttpStatus.OK); 
	 	} 

	 @RequestMapping(value = "/rynjsjfxb", method = RequestMethod.GET)
	 public  ResponseEntity<Map<String,Object>> getRynjsjfxb( 
	 			@RequestParam(value = "page", required = true) int page, 
	 			@RequestParam(value = "pageSize", required = true) int pageSize, 
	 			@RequestParam(value="where", required=false) String where){
		 return new ResponseEntity<>(xtsjfxService.getRynjsjfxb(page, pageSize, where),HttpStatus.OK);
	 }
	 
	 /**
	  * 职业证书号数据分析_主表查询
	  * @param page
	  * @param pageSize
	  * @param where
	  * @return
	  */
	 @RequestMapping(value = "/zyzshsjfxb", method = RequestMethod.GET )
	 public ResponseEntity<?> getZyzshSjfxb(
				@RequestParam(value = "pagenum", required = true) int page,
				@RequestParam(value = "pagesize", required = true) int pageSize,
				@RequestParam(value = "where", required = false) String where){
		Map<String,Object> map=xtsjfxService.getZyzshSjfxb(page, pageSize, where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
