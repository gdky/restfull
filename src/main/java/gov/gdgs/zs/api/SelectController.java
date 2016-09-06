package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SelectService;
import gov.gdgs.zs.service.XtsjfxService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 下拉选择框的数据查询
 * @author lss
 *
 */
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class SelectController {

	@Resource
	private SelectService selectService;
	
	 /**
	  * 机构下拉查询
	  * @param where
	  * @return
	  */
	 @RequestMapping(value = "/jgselect", method = RequestMethod.GET )
	 public ResponseEntity<?> getJgSelect(
				@RequestParam(value = "where", required = false) String where){
		Map<String,Object> map=selectService.getJgSelect(where);
		return new ResponseEntity<>(map, HttpStatus.OK);
	}
}
