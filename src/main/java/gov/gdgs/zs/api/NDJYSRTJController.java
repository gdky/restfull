/*
 * 年度经营收入统计
 */

package gov.gdgs.zs.api;

import java.util.Map;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.NDJYSRTJService;

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
public class NDJYSRTJController {
	@Autowired
	private NDJYSRTJService ndjysrtjService;
	@RequestMapping(value = "/ndjysrtj", method = { RequestMethod.GET })
	public ResponseEntity<?> getNdjysrtjb(
			@RequestParam(value = "nd", required = true) int nd) {
		Map<String, Object> ls = ndjysrtjService.getNdjysrtjb(nd);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}
}
