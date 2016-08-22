package gov.gdgs.zs.api;

import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.SwsService;

import java.util.Map;

import javax.annotation.Resource;
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
import org.springframework.web.multipart.MultipartFile;

import com.gdky.restfull.configuration.Constants;
import com.gdky.restfull.entity.ResponseMessage;
import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AuthService;

@RestController
@RequestMapping(value = Constants.URI_API_PREFIX + Config.URI_API_ZS)
public class SWSCXController {
	@Resource
	private SwsService swsService;
	@Autowired
	  private AuthService authService;

	@RequestMapping(value = "/jgs", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> swscx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(swsService.swscx(pn, ps, where),HttpStatus.OK);

	}
	@RequestMapping(value = "/jgs/swsslspcx", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> swsslspcx(
			@RequestParam(value = "pagenum", required = true) int pn,
			@RequestParam(value = "pagesize", required = true) int ps,
			@RequestParam(value="where", required=false) String where)  {
		
		return new ResponseEntity<>(swsService.swsslspcx(pn, ps, where),HttpStatus.OK);
		
	}

	@RequestMapping(value="/swsxx/{swsxqTab:^[A-Za-z]+$}/{swjgId}", method = { RequestMethod.GET} )
	public ResponseEntity<Map<String, Object>> swsxx(
			@PathVariable(value = "swsxqTab") String xqTab,
			@PathVariable(value = "swjgId") String gid) {
		return new ResponseEntity<>(swsService.swsxx(xqTab, gid),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/jgsxx/{jgid}", method = { RequestMethod.GET })
	public ResponseEntity<Map<String, Object>> swsxxcx(@PathVariable(value = "jgid") String jgid) {
		return new ResponseEntity<>(swsService.swsxxcx(jgid),HttpStatus.OK);
	}
	
	@RequestMapping(value = "/jggl/swsslsp", method = RequestMethod.POST)
	public ResponseEntity<?> upLoadJFSC(@RequestBody Map<String, Object> jgtj,HttpServletRequest request) throws Exception{
		jgtj.put("jgId", ((Number)swsService.insertjg(jgtj)).intValue());
		//外省主所在本省设立分所机构表PARENTJGID为0；外省事务所在本省办理业务用户设立角色为113
		boolean is=false;
		if(jgtj.containsKey("iswdzc")){
			if(jgtj.get("iswdzc").toString().equals("true")){
				is=true;
			}
		}
		if(is){
			authService.addRoleUser(113,authService.addUsers(jgtj));
		}else{
			authService.addRoleUser(4,authService.addUsers(jgtj));
		}
		return new ResponseEntity<>(true,HttpStatus.CREATED);
	}
}