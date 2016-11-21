package gov.gdgs.zs.api;

import java.util.HashMap;
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
	/**
	 * 中心端发布列表
	 * @param pn
	 * @param ps
	 * @param where
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxfbList", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> getpxfbList(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where) throws Exception  {
		
		return new ResponseEntity<>(pxmkService.getpxfbList(pn, ps, where),HttpStatus.OK);

	}
	/**
	 * 信息发布
	 * @param ptxm
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxxxapi", method = RequestMethod.POST)
	public ResponseEntity<ResponseMessage> pxxxapiInsert(
			@RequestBody Map<String,Object> ptxm,HttpServletRequest request)throws Exception {
		pxmkService.fspsq(ptxm,"pxxxfb");
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);
	}
	/**
	 * 信息修改
	 * @param ptxm
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxxxapi", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> pxxxapiUpdate(
			@RequestBody Map<String,Object> ptxm,HttpServletRequest request)throws Exception {
		pxmkService.fspsq(ptxm,"pxxxxg");
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);
	}
	/**
	 * 信息删除
	 * @param pxid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxxxapi/{pxid}", method = RequestMethod.DELETE)
	public ResponseEntity<ResponseMessage> pxxxapiDel(@PathVariable(value = "pxid") String pxid,
			HttpServletRequest request)throws Exception {
		Map<String,Object> ptxm=new HashMap<>();
		ptxm.put("pxid", pxid);
		pxmkService.fspsq(ptxm,"pxxxsc");
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);
	}
	/**
	 * 停止报名
	 * @param pxid
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxxxapi/{pxid}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> pxxxapiStop(@PathVariable(value = "pxid") String pxid,
			HttpServletRequest request)throws Exception {
		Map<String,Object> ptxm=new HashMap<>();
		ptxm.put("pxid", pxid);
		pxmkService.fspsq(ptxm,"pxxxtz");
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);
	}
	/**
	 * 获取报名列表
	 * @param pxid
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/pxxxtjlist/{pxid}", method = RequestMethod.GET)
	public ResponseEntity<?> pxtjbmList(@PathVariable(value = "pxid") String pxid)throws Exception {
		return new ResponseEntity<>(pxmkService.pxtjbmList(pxid),HttpStatus.OK);
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
		return null;
	}
	
	/*
	 * 新增培训报名
	 */
	@RequestMapping(value = "/pxbm/", method = { RequestMethod.POST })
	public ResponseEntity<?> addPxbm(
			@RequestBody Map<String,Object> values){
				return null;
	}

}
