package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SwsService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.utils.HashIdUtil;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class SummaryController {

	@Autowired
	private SwsService swsService;
	/*
	 * 获取事务所基本信息
	 */
	@RequestMapping(value = "/clisummary/{hashid}", method = { RequestMethod.GET })
	public ResponseEntity<?> getClientSummary(@PathVariable String hashid)  {
		
		//解码事务所hashid
		Long jid = HashIdUtil.decode(hashid);
		
		//获取事务所基本信息
		Map<String,Object> swsxx  = swsService.swsxx("swsxx", hashid);
		
		//获取所长信息
		Map<String,Object> szxx = swsService.getSzxx(swsxx);
		
		//获取发起人信息
		List<Map<String, Object>> fqrxx = swsService.getFqrxx(jid);
		
		//获取出资人信息
		List<Map<String, Object>> czrxx = swsService.getCzrxx(jid);
		
		Map<String,Object> summary = new HashMap<String,Object>();
		summary.put("swsxx", swsxx);
		summary.put("szxx", szxx);
		summary.put("fqrxx", fqrxx);
		summary.put("czrxx", czrxx);
		
		return ResponseEntity.ok(summary);
	}
	
	/*
	 * 获取事务所执业人员汇总列表
	 */
	@RequestMapping(value = "/sumZysws/{hashid}", method = { RequestMethod.GET })
	public ResponseEntity<?> getSumZysws(@PathVariable String hashid,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize)  {
		
		//解码事务所hashid
		Long jgId = HashIdUtil.decode(hashid);

		//获取执业人员名单
		Map<String,Object> szxx = swsService.getSumZysws(jgId,page,pagesize);
	
		return ResponseEntity.ok(szxx);

	}
	
	/*
	 * 获取事务所从业人员汇总列表
	 */
	@RequestMapping(value = "/sumCyry/{hashid}", method = { RequestMethod.GET })
	public ResponseEntity<?> getSumCyry(@PathVariable String hashid,
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize)  {
		
		//解码事务所hashid
		Long jgId = HashIdUtil.decode(hashid);

		//获取执业人员名单
		Map<String,Object> szxx = swsService.getSumCyry(jgId,page,pagesize);
	
		return ResponseEntity.ok(szxx);

	}
}
