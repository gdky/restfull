package gov.gdgs.zs.api;

import java.util.HashMap;
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

}
