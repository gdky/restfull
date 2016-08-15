/*
 * 执业资质数据分析 
 */
package gov.gdgs.zs.api;
import java.util.Map;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.ZYZZSJFXService;

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

public class ZYZZSJFXController {
	@Autowired
	private ZYZZSJFXService zyzzsjfxService;
	@RequestMapping(value = "/zyzzsjfx", method = { RequestMethod.GET })
	public ResponseEntity<?> getNdjysrtjb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value="where", required=false) String where) {
		Map<String, Object> ls = zyzzsjfxService.getZyzzsjfxb(page,pageSize,where);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}
}
