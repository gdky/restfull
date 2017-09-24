package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SDSCBBService;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.ResponseMessage;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class SDSCBBController {
	@Resource
	private SDSCBBService sdscbbService;
	
	@RequestMapping(value = "/swsjbqk1", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> swsjbqktjbcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(sdscbbService.swsjbqktjbcx(pn, ps, where),HttpStatus.OK);

	}
	@RequestMapping(value = "/hyryqk1", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> hyryqktjcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(sdscbbService.hyryqktjcx(pn, ps, where),HttpStatus.OK);
		
	}
	@RequestMapping(value = "/jysrqk1", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> jysrqktjcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(sdscbbService.jysrqktjcx(pn, ps, where),HttpStatus.OK);
		
	}
	@RequestMapping(value = "/wsbbbcx1", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> wsbbbcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(sdscbbService.wsbbbcx(pn, ps, where),HttpStatus.OK);
		
	}
	@RequestMapping(value = "/rjb1/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjb1(@PathVariable("id") String id){
		sdscbbService.rjb1(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjb2/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjb2(@PathVariable("id") String id){
		sdscbbService.rjb2(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjb4/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjb4(@PathVariable("id") String id){
		sdscbbService.rjb4(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}
	@RequestMapping(value = "/rjb6/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> rjb6(@PathVariable("id") String id){
		sdscbbService.rjb6(id);
		return new ResponseEntity<>(ResponseMessage.success("reject success"),HttpStatus.OK);
	}

}
