package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.JGSJFXService;

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

public class JGSJFXController {

	@Autowired
	private JGSJFXService jgsjfxservice;
	/*
	 * 行业学历数据分析
	 */
	@RequestMapping(value = "/hyxlsjfx", method = { RequestMethod.GET })
	public ResponseEntity<?> getHyxlsjfxb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value="where", required=false) String where) {
		Map<String, Object> ls = jgsjfxservice.getHyxlsjfxb(page,pageSize,where);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}
/*
  资金规模数据分析
 */
	@RequestMapping(value = "/zjgmsjfx", method = { RequestMethod.GET })
	public ResponseEntity<?> getZjgmsjfxb(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pageSize", required = true) int pageSize,
			@RequestParam(value="where", required=false) String where) {
		Map<String, Object> ls = jgsjfxservice.getZjgmsjfxb(page,pageSize,where);
		return new ResponseEntity<>(ls, HttpStatus.OK);

	}
}