/*
 * 执业税务师年检表
 */

package gov.gdgs.zs.api;
import gov.gdgs.zs.configuration.Config;
import gov.gdgs.zs.service.AddzyswsnjService;
import gov.gdgs.zs.service.IAddswsnjService;
import gov.gdgs.zs.service.IAddzyswsnjService;

import java.util.List;
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
public class AddzyswsnjController {
	@Resource 
	AccountService accountService;
	@Resource
	 private IAddzyswsnjService iaddzyswsnjService;
	@Resource 
	private AddzyswsnjService addzyswsnjService;
@RequestMapping(value = "/add/zyswsnj", method = RequestMethod.GET) 
	public  ResponseEntity<Map<String,Object>> getZyswsnjb( 
			
			@RequestParam(value = "page", required = true) int page, 
			@RequestParam(value = "pageSize", required = true) int pageSize, 
			@RequestParam(value="where", required=false) String where,HttpServletRequest request)
			throws Exception{ 
	    User user =  accountService.getUserFromHeaderToken(request);
		Map<String,Object> obj = addzyswsnjService.getzyswsnjb(page, pageSize, user.getJgId(), where);
				
		return new ResponseEntity<>(obj,HttpStatus.OK); 
	}
//姓名下拉框选择显示信息
@RequestMapping(value = "/add/zyswsnj1/{sws_id}", method = RequestMethod.GET)
public ResponseEntity<Map<String, Object>> getZyswsnjBySwsId(
	@PathVariable("sws_id") String sws_id) {

Map<String, Object> obj = addzyswsnjService.getzyswsnjbBySwsId(sws_id);
return new ResponseEntity<>(obj, HttpStatus.OK);
}

//点击查看时显示信息
@RequestMapping(value = "/add/zyswsnj/{id}", method = RequestMethod.GET)
public ResponseEntity<Map<String, Object>> getZyswsnjById(
	@PathVariable("id") String id) {

Map<String, Object> obj = addzyswsnjService.getzyswsnjbById(id);
return new ResponseEntity<>(obj, HttpStatus.OK);
}


//执业税务师姓名的选择
@RequestMapping(value="/add/zyswsxm",method=RequestMethod.GET)
public ResponseEntity<Map<String,Object>> getZyswsxm(
		@RequestParam(value = "page", required = true) int page, 
		@RequestParam(value = "pageSize", required = true) int pageSize,
		@RequestParam(value="where", required=false) String where,HttpServletRequest request)
		throws Exception{ 
    User user =  accountService.getUserFromHeaderToken(request);
	Map<String,Object> obj = addzyswsnjService.getzyswsxm(page, pageSize,user.getJgId(), where);
			
	return new ResponseEntity<>(obj,HttpStatus.OK); 
}

//报备份数（bafs）
@RequestMapping(value = "/add/zyswsnj2", method = RequestMethod.GET)
public ResponseEntity<Map<String, Object>> getZyswsnjBafs(
		@RequestParam(value="nd", required=false) String nd,
		@RequestParam(value="sws_id", required=false) String sws_id,
		HttpServletRequest request)
	 {
	
Map<String, Object> obj = addzyswsnjService.getzyswsnjBafs(nd, sws_id);
return new ResponseEntity<>(obj, HttpStatus.OK);

}
//执业税务师年检年度校验
@RequestMapping(value="checkzyswsnjnd",method = RequestMethod.GET)
public ResponseEntity<Map<String, Object>> checkzyswsnjnd(
		@RequestParam(value="where", required=false) String where,
		HttpServletRequest request)
		{
		
    User user =  accountService.getUserFromHeaderToken(request);
Map<String, Object> obj = addzyswsnjService.checkzyswsnjnd(user.getJgId(), where);
				return new ResponseEntity<>(obj,HttpStatus.OK); 
}




//执业税务师年检表增加
@RequestMapping(value = "/addzyswsnjb", method = RequestMethod.POST)
	public ResponseEntity<Map<String,Object>> addZyswsnjb(@RequestBody  Map<String ,Object> obj,HttpServletRequest request)
	throws Exception{
		 try{
			 User user =  accountService.getUserFromHeaderToken(request);
			 obj.put("use_id",user.getId());
			 obj.put("jg_id", user.getJgId());
		 }catch (Exception e){	 
		 }
		Map<String,Object> rs = iaddzyswsnjService.addZyswsnjb(obj);
		return new ResponseEntity<>(rs,HttpStatus.CREATED);
	}
//更新执业税务师年检表
@RequestMapping(value = "/addzyswsnjb/{id}", method = RequestMethod.PUT)
	public ResponseEntity<ResponseMessage> updateZyswsnjb(@PathVariable("id") String id,
			@RequestBody Map <String,Object> obj,HttpServletRequest request) 
			throws Exception{
		try{
			 User user =  accountService.getUserFromHeaderToken(request);
			 obj.put("use_id",user.getId());
			 obj.put("jg_id", user.getJgId());
		 }catch (Exception e){	 
		 }
		
		addzyswsnjService.updateZyswsnjb(obj);
		return new ResponseEntity<>(ResponseMessage.success("更新成功"),HttpStatus.OK);

	}


}

