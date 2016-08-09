/*
 * 年度鉴证情况统计表
 */
package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.NDJZQKTJService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;


@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)

public class NDJZQKTJController {

	@Autowired
	private NDJZQKTJService ndjzqktjservice;
	@RequestMapping(value = "/ndjzqktj", method = { RequestMethod.GET })
	public ResponseEntity<?> getSwszttjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value="where", required=false) String where) {
		Map<String, Object> ls = ndjzqktjservice.getNdjzqktjb(page,pageSize,where);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}

	
}

