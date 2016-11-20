package gov.gdgs.zs.api;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.PXMKService;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.gdky.restfull.service.AccountService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class PXMKController {
	@Resource
	private PXMKService pxmkService;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private AccountService accountService;
	
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
	
	/*
	 * 客户端用培训信息列表
	 */
	@RequestMapping(value = "/pxxx", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> getPxxx(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String whereparam)  {
		
		User user = accountService.getUserFromHeaderToken(request);
		return new ResponseEntity<>(pxmkService.getPxxx(user,page, pagesize, whereparam),HttpStatus.OK);
	}
	/*
	 * 培训内容
	 */
	@RequestMapping(value = "/pxnr/{id}", method = { RequestMethod.GET })
	public ResponseEntity<?> getPxnr(
			@PathVariable String id)  {
		return new ResponseEntity<>(pxmkService.getPxnr(id),HttpStatus.OK);
	}
	
	/*
	 * 培训报名初始化信息
	 */
	@RequestMapping(value = "/pxbminit/{id}", method = { RequestMethod.GET })
	public ResponseEntity<?> getPxbmInit(@PathVariable String id){
		User user = accountService.getUserFromHeaderToken(request);
		Map<String,Object> rs = pxmkService.getPxbmInit(user,id);
		return new ResponseEntity<>(rs,HttpStatus.OK);
	}
	
	/*
	 * 新增培训报名
	 */
	@RequestMapping(value = "/pxbm/{pxid}", method = { RequestMethod.POST })
	public ResponseEntity<?> addPxbm(
			@RequestBody List<Map<String,Object>> values,
			@PathVariable String pxid){
		User user = accountService.getUserFromHeaderToken(request);
		pxmkService.addPxbm(user,pxid,values);
				return null;
	}

}
