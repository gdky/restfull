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
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.utils.HashIdUtil;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class SummaryController {

	@Autowired
	private SwsService swsService;
	
	@RequestMapping(value = "/summary/{hashid}", method = { RequestMethod.GET })
	public ResponseEntity<?> getClientSumnmary(@PathVariable String hashid)  {
		
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
	
	@RequestMapping(value = "/sumryxx/", method = { RequestMethod.GET })
	public ResponseEntity<?> getClientSumnmary(@PathVariable String hashid)  {
		
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
}
