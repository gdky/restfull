package gov.gdgs.zs.api;


import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SwsqkTjAService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 系统统计报表——事务所情况统计A
 * @author lss
 *
 */
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class SwsqkTjAController {

	@Resource
	private SwsqkTjAService swsqkTjAService;
	
	@RequestMapping(value = "/swsqktjA", method = RequestMethod.GET )
	public ResponseEntity<?> getSwsqkTjAs(
			@RequestParam(value = "pagenum", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pageSize,
			@RequestParam(value = "where", required = false) String where){
		Map<String,Object> map=swsqkTjAService.getSwsqkTjAs(page, pageSize, where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value="/swsqktjA/{swsqktjTab:^[A-Za-z]+$}/{swsqktjId}", method = RequestMethod.GET )
	public ResponseEntity<Map<String, Object>> swsxx(
			@PathVariable(value = "swsqktjTab") String xqTab,
			@PathVariable(value = "swsqktjId") String gid) {
		Map<String, Object> map=swsqkTjAService.kzxx(xqTab, gid);
		return new ResponseEntity<>(map,HttpStatus.OK);		
	}
	
}
