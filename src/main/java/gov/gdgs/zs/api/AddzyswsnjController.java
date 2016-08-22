/*
 * 执业税务师年检表
 */

package gov.gdgs.zs.api;
import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.AddzyswsnjService;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gdky.restfull.entity.User;
import com.gdky.restfull.service.AccountService;
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class AddzyswsnjController {
	@Resource 
	AccountService accountService;
	@Resource 
	private AddzyswsnjService addzyswsnjService;
@RequestMapping(value = "/add/zyswsnj", method = RequestMethod.GET) 
	public  ResponseEntity<Map<String,Object>> getZyswsnjb( 
			
			@RequestParam(value = "page", required = true) int page, 
			@RequestParam(value = "pageSize", required = true) int pageSize, 
			@RequestParam(value="where", required=false) String where,HttpServletRequest request)
			throws Exception{ 
	    User user =  accountService.getUserFromHeaderToken(request);
		Map<String,Object> obj = addzyswsnjService.getzyswsnjb(page, pageSize, user.getId(), where);
				
		return new ResponseEntity<>(obj,HttpStatus.OK); 
	}

}

