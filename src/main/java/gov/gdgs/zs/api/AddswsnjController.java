/*
 * author:sunZ
 * 事务所年检增加
 */
package gov.gdgs.zs.api;
import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.AddcwbbService;
import gov.gdgs.zs.service.AddswsnjService;
import gov.gdgs.zs.service.IAddcwbbService;
import gov.gdgs.zs.service.IAddsdsbService;
import gov.gdgs.zs.service.IAddswsnjService;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

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
@RestController
@RequestMapping(value = Config.URL_PROJECT)
public class AddswsnjController {
	@Resource 
	AccountService accountService;
	 @Resource
	 private IAddswsnjService iaddswsnjService;
	@Resource 
	private AddswsnjService addswsnjService;
	
@RequestMapping(value = "/add/swsnj", method = RequestMethod.GET) 
	public  ResponseEntity<Map<String,Object>> getSwsnjb( 
			
			@RequestParam(value = "page", required = true) int page, 
			@RequestParam(value = "pageSize", required = true) int pageSize, 
			@RequestParam(value="where", required=false) String where,HttpServletRequest request)
			throws Exception{ 
	User user =  accountService.getUserFromHeaderToken(request);
	
		Map<String,Object> obj = addswsnjService.getswsnjb(page, pageSize, user.getJgId(), where);
				
		return new ResponseEntity<>(obj,HttpStatus.OK); 
	}

   @RequestMapping(value = "/add/swsnj/{id}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> getSwsnjById(
		@PathVariable("id") String id) {

	Map<String, Object> obj = addswsnjService.getSwsnjById(id);
	return new ResponseEntity<>(obj, HttpStatus.OK);
}
//事务所年检表增加
   @RequestMapping(value = "/addswsnjb", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addSwsnjb(@RequestBody  Map<String ,Object> obj,HttpServletRequest request)
	throws Exception{
		 try{
			 User user =  accountService.getUserFromHeaderToken(request);
			 obj.put("use_id",user.getId());
			 obj.put("jg_id", user.getJgId());
		 }catch (Exception e){	 
		 }
		Map<String,Object> rs = iaddswsnjService.addSwsnjb(obj);
		return new ResponseEntity<>(rs,HttpStatus.CREATED);
	}
   //更新事务所年检表
   @RequestMapping(value = "/addswsnjb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateSwsnjb(@PathVariable("id") String id,
			@RequestBody Map <String,Object> obj,HttpServletRequest request) 
			throws Exception{
		try{
			 User user =  accountService.getUserFromHeaderToken(request);
			 obj.put("use_id",user.getId());
			 obj.put("jg_id", user.getJgId());
		 }catch (Exception e){	 
		 }
		
		addswsnjService.updateSwsnjb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);

	}
   
   

}