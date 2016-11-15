package gov.gdgs.zs.api;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.PXMKService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.User;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class PXMKController {
	@Resource
	private PXMKService pxmkService;
	
	@RequestMapping(value = "/getpxfbList", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> getpxfbList(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where) throws Exception  {
		
		return new ResponseEntity<>(pxmkService.getpxfbList(pn, ps, where),HttpStatus.OK);

	}
	@RequestMapping(value = "/pxxxapi/{splx}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updatePTXM(@PathVariable(value = "splx") String splx,
			@RequestBody Map<String,Object> ptxm,HttpServletRequest request)throws Exception {
		pxmkService.fspsq(ptxm,splx);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);
	}

}
