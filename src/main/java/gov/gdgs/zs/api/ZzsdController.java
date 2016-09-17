package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.ZzsdService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;


/**
 * 资质锁定api
 * @author admin
 *
 */
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class ZzsdController {
	
	@Autowired
	private ZzsdService zzsdService;
	
	@Autowired
	private AccountService accountService;
	
	/**
	 * 获取有效的机构锁定记录
	 */
	@RequestMapping(value = "/jgzzsd", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getJgZzsd(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = zzsdService.getJgZzsd(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	/**
	 * 获取失效的机构锁定记录（机构资质锁定无效）
	 */
	@RequestMapping(value = "/jgzzsdwx", method = RequestMethod.GET)
	public  ResponseEntity<Map<String,Object>> getJgZzsdwx(
			@RequestParam(value = "page", required = true) int page,
			@RequestParam(value = "pagesize", required = true) int pagesize,
			@RequestParam(value="where", required=false) String where){ 

		Map<String,Object> obj = zzsdService.getJgZzsdwx(page,pagesize,where);
		return new ResponseEntity<>(obj,HttpStatus.OK);
	}
	
	/*
	 * 添加机构锁定记录
	 */
	@RequestMapping(value = "/jgzzsd", method = RequestMethod.POST)
	public  ResponseEntity<?> addJgZzsd(
			@RequestBody Map<String,Object> rqbody,
			HttpServletRequest request){ 
		User user =  accountService.getUserFromHeaderToken(request);
		Integer lx = (Integer)rqbody.get("lx");
		String sdyy = (String)rqbody.get("sdyy");
		List<String> jgId = (List<String>) rqbody.get("jgId");
		zzsdService.addJgZzsd(user,sdyy,jgId,lx);
		ResponseMessage rm  = new ResponseMessage(ResponseMessage.Type.success, "200", "更新成功");
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	
	/*
	 * 修改机构锁定记录
	 */
	@RequestMapping(value = "/jgzzsd", method = RequestMethod.PUT)
	public  ResponseEntity<?> updateJgZzsd(
			@RequestBody Map<String,Object> rqbody,
			HttpServletRequest request){ 
		User user =  accountService.getUserFromHeaderToken(request);
		List<Integer> id = (List<Integer>) rqbody.get("id");
		zzsdService.updateJgZzsd(user,id);
		ResponseMessage rm  = new ResponseMessage(ResponseMessage.Type.success, "200", "更新成功");
		return new ResponseEntity<>(rm,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/zzsdhave", method = RequestMethod.POST)
	public  ResponseEntity<?> zzsdhave(
			@RequestParam Integer lx,
			@RequestBody Map<String,Object> rqbody){ 
		List<Integer> jgId = (List<Integer>) rqbody.get("jgId");
		List<Integer> ls = zzsdService.getSdJGByLx(lx);
		ArrayList<Integer> rs = new ArrayList<Integer>();
		for(Integer id : jgId){
			if(!ls.contains(id)) {
				rs.add(id);
			}
		}
		return new ResponseEntity<>(rs,HttpStatus.OK);
	}
	
}
