package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.HyryqkTjService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 行业人员情况统计
 * @author lss
 *
 */
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class HyryqkTjController {

	@Resource
	private HyryqkTjService hyryqkTjService;

	@RequestMapping(value = "/hyryqktj", method = RequestMethod.GET )
	public ResponseEntity<?> getHyryqkTj(
			@RequestParam(value="where",required=false) String where){
		Map<String,Object> map=hyryqkTjService.getHyryqkTj(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/hyryqktjMx", method = RequestMethod.GET )
	public ResponseEntity<?> getHyryqkTjMx(
			@RequestParam(value="type",required=false) String type,
			@RequestParam(value="lx",required=false) String lx,
			@RequestParam(value="where",required=false) String where){
		Map<String,Object> map=hyryqkTjService.getHyryqkTjMx(type,lx,where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
	
}
