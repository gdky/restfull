package gov.gdgs.zs.api;

import java.util.Map;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.YhdljlbService;

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
public class YhdljlbController {
	
	@Autowired
	private YhdljlbService yhdljlbservice;
	@RequestMapping(value = "/yhdljlb", method = { RequestMethod.GET })
	public ResponseEntity<?> getYhdljlb(@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pageSize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)
			 {
		Map<String, Object> ls = yhdljlbservice.getYhdljlb(pn, ps, where);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}

}
